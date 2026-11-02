# LifeOS

LifeOS is a mobile-first, freemium personal operating system for habits, life areas, goals, planning and personal insights.

## Product decisions

- Platforms: iOS and Android (mobile first)
- Product: SaaS, freemium
- Design: custom, Strata-inspired; do not copy proprietary assets or UI
- Theme: adaptive light/dark with manual override
- Android: Kotlin + Jetpack Compose (native)
- iOS: Swift + SwiftUI (native; iOS 26 Liquid Glass where appropriate)
- Design system: Lumen native Compose and SwiftUI components
- Backend: ASP.NET Core 10 / C# 14
- Persistence: PostgreSQL + Entity Framework Core
- Architecture: modular monolith, offline-first mobile experience
- API: versioned REST with OpenAPI contracts

## Repository layout

- `apps/android` — native Kotlin / Jetpack Compose application
- `apps/ios` — native Swift / SwiftUI application
- `src/backend` — .NET solution and API
- `contracts/openapi` — shared API contract artifacts
- `docs/architecture` — architecture decisions and diagrams
- `docs/product` — product requirements and UX decisions

## Parallel delivery

Frontend and backend work proceed independently against the shared OpenAPI contract. The mobile app must support mock API mode until backend endpoints are available. Changes land through focused feature branches and pull requests.

## Initial milestone

1. Establish architecture, coding standards and API contracts.
2. Build native Android and iOS design systems, navigation and dashboard shells.
3. Build backend solution, health endpoint, persistence foundation and first domain modules.
4. Integrate the dashboard and habit workflows end-to-end.

## Engineering principles

- Keep business rules in the domain/application layers.
- Use UTC timestamps for instants and explicit IANA time-zone identifiers for user-local schedules.
- Make habit completion idempotent and preserve completion history.
- Do not calculate streaks by destructive resets; derive them from immutable completion history and habit schedules.
- Never commit secrets, credentials or production personal data.
- Add tests for business rules and API behavior.
