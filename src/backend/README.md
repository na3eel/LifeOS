# LifeOS Backend

ASP.NET Core 10 modular-monolith foundation.

## Projects

- `LifeOS.Api` — HTTP boundary and composition root.
- Planned: Domain, Application, Infrastructure and Worker projects.

## Local run

Requires the .NET 10 SDK.

```bash
dotnet run --project LifeOS.Api
```

Health endpoint: `GET /health`.

Database, authentication and domain modules will be introduced in subsequent commits. Do not add secrets to appsettings committed to source control.