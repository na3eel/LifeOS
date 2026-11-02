# Architecture decisions

## ADR-001: Mobile-first React Native application
Use React Native with Expo and TypeScript for Android and iOS. Keep feature code organized by domain and share API contracts through OpenAPI.

## ADR-002: ASP.NET Core modular monolith
Use ASP.NET Core 10 and C# 14. Keep Domain, Application, Infrastructure, API and Worker concerns separate. Do not split into microservices until operational evidence justifies it.

## ADR-003: PostgreSQL and EF Core
PostgreSQL is the system of record. EF Core owns persistence mapping and migrations. Use unique constraints for idempotent completion records.

## ADR-004: Offline-first mobile
Persist user actions locally, render optimistic UI, queue idempotent commands and reconcile with server state. Conflicts must be explicit and never silently erase user history.

## ADR-005: Timezone-aware habit scheduling
Persist instants in UTC. Persist user IANA timezone identifiers and habit-local dates separately. Expected occurrences derive from schedule rules, not month length.

## ADR-006: Adaptive design
Follow system appearance by default, with an explicit user override. Keep semantic color tokens rather than hardcoded colors inside feature screens.

## ADR-007: Privacy and security
Every user-owned record is tenant-scoped by authenticated user identity. Do not ship personal-data endpoints without authorization. AI receives only minimum necessary, consented context.
