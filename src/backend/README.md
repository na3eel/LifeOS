# LifeOS Backend

ASP.NET Core 10 modular-monolith foundation.

## Projects
- `LifeOS.Api` — HTTP boundary and composition root.
- `LifeOS.Domain` — domain entities and invariants.
- `LifeOS.Application` — use cases and orchestration.
- `LifeOS.Infrastructure` — EF Core / PostgreSQL persistence.

## Local run
Requires the .NET 10 SDK.

```bash
cd src/backend
dotnet restore LifeOS.sln
dotnet build LifeOS.sln
dotnet run --project LifeOS.Api
```

Health endpoint: `GET /health`; API metadata: `GET /api/v1/meta`.

Configure the database connection using the `ConnectionStrings__LifeOS` environment variable. Do not commit credentials.

## Docker
Build from this directory: `docker build -t lifeos-api .`.

Database migrations and authenticated domain endpoints are still pending. Do not expose user-specific data endpoints until authentication and ownership authorization are implemented.
