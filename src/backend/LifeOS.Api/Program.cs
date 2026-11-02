var builder = WebApplication.CreateBuilder(args);

builder.Services.AddOpenApi();

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

app.MapGet("/", () => Results.Redirect("/health"));

app.Run();

public partial class Program;