using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.RateLimiting;
using LifeOS.Api.Services;
using LifeOS.Infrastructure.Identity;
using LifeOS.Infrastructure.Persistence;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.RateLimiting;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddOpenApi();
builder.Services.AddProblemDetails();
builder.Services.AddRateLimiter(options =>
{
    options.RejectionStatusCode = StatusCodes.Status429TooManyRequests;
    options.AddPolicy("auth", httpContext =>
        RateLimitPartition.GetFixedWindowLimiter(
            partitionKey: httpContext.Connection.RemoteIpAddress?.ToString() ?? "unknown",
            factory: _ => new FixedWindowRateLimiterOptions
            {
                PermitLimit = 10,
                Window = TimeSpan.FromMinutes(1),
                QueueLimit = 0,
                AutoReplenishment = true
            }));
});

var connectionString = builder.Configuration.GetConnectionString("LifeOS")
    ?? throw new InvalidOperationException("ConnectionStrings:LifeOS must be configured.");
var signingKey = builder.Configuration["Authentication:SigningKey"]
    ?? throw new InvalidOperationException("Authentication:SigningKey must be configured.");
if (Encoding.UTF8.GetByteCount(signingKey) < 32)
    throw new InvalidOperationException("Authentication:SigningKey must be at least 32 bytes.");

var issuer = builder.Configuration["Authentication:Issuer"] ?? "LifeOS";
var audience = builder.Configuration["Authentication:Audience"] ?? "LifeOS.Mobile";

builder.Services.AddDbContext<LifeOsDbContext>(options => options.UseNpgsql(connectionString));
builder.Services
    .AddIdentityCore<LifeOsUser>(options =>
    {
        options.User.RequireUniqueEmail = true;
        options.SignIn.RequireConfirmedEmail = true;
        options.Password.RequiredLength = 10;
        options.Password.RequireDigit = true;
        options.Password.RequireLowercase = true;
        options.Password.RequireUppercase = true;
        options.Password.RequireNonAlphanumeric = true;
        options.Lockout.MaxFailedAccessAttempts = 5;
        options.Lockout.DefaultLockoutTimeSpan = TimeSpan.FromMinutes(15);
        options.Lockout.AllowedForNewUsers = true;
    })
    .AddRoles<IdentityRole<Guid>>()
    .AddEntityFrameworkStores<LifeOsDbContext>()
    .AddSignInManager()
    .AddDefaultTokenProviders();
builder.Services.AddScoped<IEmailSender, SmtpEmailSender>();

var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(signingKey));
builder.Services
    .AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidIssuer = issuer,
            ValidateAudience = true,
            ValidAudience = audience,
            ValidateIssuerSigningKey = true,
            IssuerSigningKey = key,
            ValidateLifetime = true,
            ClockSkew = TimeSpan.FromSeconds(30),
            NameClaimType = ClaimTypes.NameIdentifier
        };
    });
builder.Services.AddAuthorization();

var app = builder.Build();

if (app.Environment.IsDevelopment())
    app.MapOpenApi();

app.UseExceptionHandler();
app.UseRateLimiter();
app.UseAuthentication();
app.UseAuthorization();

app.MapGet("/health", () => Results.Ok(new
{
    status = "healthy",
    service = "LifeOS.Api",
    version = "v1"
})).WithName("HealthCheck");

app.MapGet("/api/v1/meta", () => Results.Ok(new
{
    name = "LifeOS",
    apiVersion = "v1",
    persistenceConfigured = true
})).WithName("ApiMetadata");

var auth = app.MapGroup("/api/v1/auth").WithTags("Authentication").RequireRateLimiting("auth");

auth.MapPost("/register", async (
    RegisterRequest request,
    UserManager<LifeOsUser> users,
    IEmailSender emailSender,
    CancellationToken cancellationToken) =>
{
    var email = request.Email?.Trim();
    var displayName = request.DisplayName?.Trim();

    if (string.IsNullOrWhiteSpace(email) || !new System.ComponentModel.DataAnnotations.EmailAddressAttribute().IsValid(email))
        return Results.ValidationProblem(new Dictionary<string, string[]> { ["email"] = ["A valid email address is required."] });
    if (string.IsNullOrWhiteSpace(displayName) || displayName.Length > 80)
        return Results.ValidationProblem(new Dictionary<string, string[]> { ["displayName"] = ["Display name is required and must be 80 characters or fewer."] });
    if (string.IsNullOrEmpty(request.Password))
        return Results.ValidationProblem(new Dictionary<string, string[]> { ["password"] = ["Password is required."] });

    var user = new LifeOsUser
    {
        Id = Guid.NewGuid(),
        UserName = email,
        Email = email,
        DisplayName = displayName,
        CreatedAt = DateTimeOffset.UtcNow
    };

    var result = await users.CreateAsync(user, request.Password);
    if (!result.Succeeded)
    {
        var errors = result.Errors
            .GroupBy(error => error.Code.StartsWith("Duplicate", StringComparison.Ordinal) ? "email" : "password")
            .ToDictionary(group => group.Key, group => group.Select(error => error.Description).ToArray());
        return Results.ValidationProblem(errors);
    }

    var token = await users.GenerateEmailConfirmationTokenAsync(user);
    var link = EmailLinkBuilder.Build(builder.Configuration, "confirm-email", email, token);
    await emailSender.SendAsync(email, "Verify your LifeOS email", $"<p>Welcome to LifeOS, {System.Net.WebUtility.HtmlEncode(displayName)}.</p><p><a href=\"{System.Net.WebUtility.HtmlEncode(link)}\">Verify email address</a></p>", cancellationToken);
    return Results.Accepted(value: new { message = "Account created. Check your email to verify your address before logging in." });
})
.AllowAnonymous()
.WithName("Register");

auth.MapPost("/login", async (
    LoginRequest request,
    UserManager<LifeOsUser> users,
    SignInManager<LifeOsUser> signInManager) =>
{
    if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrEmpty(request.Password))
        return Results.BadRequest(new { message = "Email and password are required." });

    var user = await users.FindByEmailAsync(request.Email.Trim());
    if (user is null)
        return Results.Unauthorized();

    var result = await signInManager.CheckPasswordSignInAsync(user, request.Password, lockoutOnFailure: true);
    if (result.IsNotAllowed)
        return Results.Problem(statusCode: StatusCodes.Status403Forbidden, title: "Email verification required", detail: "Verify your email address before signing in.");
    if (result.IsLockedOut)
        return Results.Problem(statusCode: StatusCodes.Status423Locked, title: "Account temporarily locked", detail: "Too many failed attempts. Try again later.");
    if (!result.Succeeded)
        return Results.Unauthorized();

    return Results.Ok(IssueToken(user, issuer, audience, key));
})
.AllowAnonymous()
.WithName("Login");

auth.MapPost("/confirm-email", async (
    ConfirmEmailRequest request,
    UserManager<LifeOsUser> users) =>
{
    if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.Token))
        return Results.BadRequest(new { message = "Email and verification token are required." });
    var user = await users.FindByEmailAsync(request.Email.Trim());
    if (user is null) return Results.BadRequest(new { message = "The verification link is invalid or expired." });
    var result = await users.ConfirmEmailAsync(user, request.Token);
    return result.Succeeded
        ? Results.Ok(new { message = "Email verified. You can now sign in." })
        : Results.BadRequest(new { message = "The verification link is invalid or expired." });
}).AllowAnonymous().WithName("ConfirmEmail");

auth.MapPost("/resend-verification", async (
    EmailOnlyRequest request,
    UserManager<LifeOsUser> users,
    IEmailSender emailSender,
    IConfiguration configuration,
    CancellationToken cancellationToken) =>
{
    if (string.IsNullOrWhiteSpace(request.Email) || !new System.ComponentModel.DataAnnotations.EmailAddressAttribute().IsValid(request.Email))
        return Results.BadRequest(new { message = "A valid email address is required." });
    var user = await users.FindByEmailAsync(request.Email.Trim());
    if (user is not null && !user.EmailConfirmed)
    {
        var token = await users.GenerateEmailConfirmationTokenAsync(user);
        var link = EmailLinkBuilder.Build(configuration, "confirm-email", user.Email!, token);
        await emailSender.SendAsync(user.Email!, "Verify your LifeOS email", $"<p><a href=\"{System.Net.WebUtility.HtmlEncode(link)}\">Verify email address</a></p>", cancellationToken);
    }
    return Results.Ok(new { message = "If the account exists and needs verification, an email has been sent." });
}).AllowAnonymous().WithName("ResendVerification");

auth.MapPost("/forgot-password", async (
    EmailOnlyRequest request,
    UserManager<LifeOsUser> users,
    IEmailSender emailSender,
    IConfiguration configuration,
    CancellationToken cancellationToken) =>
{
    if (string.IsNullOrWhiteSpace(request.Email) || !new System.ComponentModel.DataAnnotations.EmailAddressAttribute().IsValid(request.Email))
        return Results.BadRequest(new { message = "A valid email address is required." });
    var user = await users.FindByEmailAsync(request.Email.Trim());
    if (user is not null && await users.IsEmailConfirmedAsync(user))
    {
        var token = await users.GeneratePasswordResetTokenAsync(user);
        var link = EmailLinkBuilder.Build(configuration, "reset-password", user.Email!, token);
        await emailSender.SendAsync(user.Email!, "Reset your LifeOS password", $"<p><a href=\"{System.Net.WebUtility.HtmlEncode(link)}\">Reset password</a></p>", cancellationToken);
    }
    return Results.Ok(new { message = "If the account exists, a password reset email has been sent." });
}).AllowAnonymous().WithName("ForgotPassword");

auth.MapPost("/reset-password", async (
    ResetPasswordRequest request,
    UserManager<LifeOsUser> users) =>
{
    if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.Token) || string.IsNullOrWhiteSpace(request.NewPassword))
        return Results.BadRequest(new { message = "Email, reset token and new password are required." });
    var user = await users.FindByEmailAsync(request.Email.Trim());
    if (user is null) return Results.BadRequest(new { message = "The reset link is invalid or expired." });
    var result = await users.ResetPasswordAsync(user, request.Token, request.NewPassword);
    if (!result.Succeeded)
        return Results.ValidationProblem(result.Errors.GroupBy(_ => "password").ToDictionary(g => g.Key, g => g.Select(e => e.Description).ToArray()));
    return Results.Ok(new { message = "Password updated. Sign in with your new password." });
}).AllowAnonymous().WithName("ResetPassword");

auth.MapGet("/me", async (ClaimsPrincipal principal, UserManager<LifeOsUser> users) =>
{
    var user = await users.GetUserAsync(principal);
    return user is null
        ? Results.Unauthorized()
        : Results.Ok(new CurrentUserResponse(user.Id, user.Email!, user.DisplayName, user.CreatedAt));
})
.RequireAuthorization()
.WithName("CurrentUser");

app.MapGet("/", () => Results.Redirect("/health"));

app.Run();

static AuthResponse IssueToken(LifeOsUser user, string issuer, string audience, SymmetricSecurityKey key)
{
    var now = DateTime.UtcNow;
    var expires = now.AddHours(1);
    var claims = new[]
    {
        new Claim(JwtRegisteredClaimNames.Sub, user.Id.ToString()),
        new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
        new Claim(JwtRegisteredClaimNames.Email, user.Email!),
        new Claim(ClaimTypes.Name, user.DisplayName),
        new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
    };
    var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
    var token = new JwtSecurityToken(issuer, audience, claims, now, expires, credentials);
    return new AuthResponse(
        new JwtSecurityTokenHandler().WriteToken(token),
        "Bearer",
        expires,
        new CurrentUserResponse(user.Id, user.Email!, user.DisplayName, user.CreatedAt));
}

public sealed record RegisterRequest(string? Email, string? Password, string? DisplayName);
public sealed record LoginRequest(string? Email, string? Password);
public sealed record EmailOnlyRequest(string? Email);
public sealed record ConfirmEmailRequest(string? Email, string? Token);
public sealed record ResetPasswordRequest(string? Email, string? Token, string? NewPassword);
public sealed record CurrentUserResponse(Guid Id, string Email, string DisplayName, DateTimeOffset CreatedAt);
public sealed record AuthResponse(string AccessToken, string TokenType, DateTime ExpiresAt, CurrentUserResponse User);

public partial class Program { }
