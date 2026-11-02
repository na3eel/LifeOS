using LifeOS.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddOpenApi();
builder.Services.AddProblemDetails();

var connectionString = builder.Configuration.GetConnectionString("LifeOS");
if (!string.IsNullOrWhiteSpace(connectionString))
{
    builder.Services.AddDbContext<LifeOsDbContext>(options =>
        options.UseNpgsql(connectionString));
}

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

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
    persistenceConfigured = !string.IsNullOrWhiteSpace(connectionString)
})).WithName("ApiMetadata");

app.MapGet("/", () => Results.Redirect("/health"));

app.Run();

public partial class Program;