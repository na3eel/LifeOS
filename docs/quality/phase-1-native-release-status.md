# Phase 1 — Native Mobile Release Status

**Last reviewed:** 2026-11-03  
**Repository:** `na3eel/LifeOS`  
**Status:** In progress — do not declare Phase 1 complete until all release gates below pass.

## Working branches and pull requests

- Android: `work/android-native-compose` — [PR #3](https://github.com/na3eel/LifeOS/pull/3)
- iOS: `work/ios-native-swiftui` — [PR #4](https://github.com/na3eel/LifeOS/pull/4)
- Backend foundation: `work/backend-foundation` — [PR #2](https://github.com/na3eel/LifeOS/pull/2)

The native PRs were observed as open drafts and GitHub reported their mergeability state as dirty during this review. Re-check the base/head relationship and resolve conflicts before attempting integration. Do not merge just to close the phase.

## Confirmed implementation

### Android
- Kotlin + Jetpack Compose native application.
- Room persistence for habits, date-keyed habit completions, areas, tasks and goals.
- Room migrations through schema version 4.
- Habit repeat-day and target configuration; date navigator; task due dates.
- Five primary destinations: Today, Areas, Calendar, Insights and Coach.
- Room DAO instrumentation tests cover CRUD, completion idempotency/date scoping, goals, tasks and areas.
- Android GitHub Actions workflow assembles a debug APK and runs emulator instrumentation tests.

### iOS
- SwiftUI + SwiftData native application.
- Models for habits, date-keyed habit completions, areas, tasks and goals.
- Today view has date navigation, scheduled habits, completion tracking and habit editor/presets.
- Five primary destinations: Today, Areas, Plan, Insights and Coach.
- SwiftData in-memory persistence tests cover CRUD, recurring habit fields, date-scoped completions, areas, tasks and goals.
- iOS GitHub Actions workflow generates the Xcode project and builds/tests the simulator app.

### Design
- Approved Strata tokens are documented in `docs/design/lifeos-approved-theme.md`.
- On 2026-11-03 the native light-theme palette was aligned to the approved canvas `#FFFBF7`, card `#F3E8E1`, border `#D6C5BC`, text `#1F1724`, primary `#DB5635`, and secondary `#9828F6`.
- Dark themes remain intentional native adaptations; keep solid surfaces and do not introduce glassmorphism or gradients.

## Changes made during the 2026-11-03 review

- Android theme token alignment: commit `9ffe52819ddbd2edc635edcfb59331b0071df1fe`.
- Android Coach now bases its completion message on the selected date's completion records rather than the legacy habit-level `done` flag: commit `c6cc89f4cd41370bb907e2b9669056257d30dfe5`.
- iOS semantic palette alignment: commit `0d1ea52e1b257d2f653eb504329fbd7033de63b1`.
- iOS Lumen root palette alignment: commit `926f99f491642135c406a2fe02dac3140d576462`.
- iOS daily progress summary now reads date-scoped `HabitCompletion` records rather than the legacy `Habit.isComplete` property: commit `10f7fd65df47e06f8e833dd5fabf5e7e15805299`.

## Backend and integration findings

The backend foundation includes ASP.NET Core Identity registration, email confirmation, login, resend-verification, forgot/reset-password, JWT validation, rate limiting and an authenticated `/api/v1/auth/me` endpoint. The backend README explicitly says database migrations and authenticated domain endpoints are pending.

The native clients have not yet been verified as integrated with those endpoints. The current login token is a one-hour access token; refresh-token/session renewal and secure mobile token storage need an explicit design and implementation. Do not claim authentication/session restoration is complete.

## Release blockers / next work

1. Resolve PR #3 and PR #4 merge conflicts/dirty mergeability; compare each branch against current `main` before integration.
2. Verify latest Android emulator instrumentation run and latest iOS simulator build/test run after the 2026-11-03 palette and completion-summary commits.
3. Add migration tests that open historical Room schemas (v1, v2, v3) and verify the v1→v4 upgrade preserves user data. Current DAO tests use an in-memory current schema and do not prove migration safety.
4. Review SwiftData model evolution and data preservation across app upgrades.
5. Integrate native sign-up/login/email verification/password reset/current-user flows with backend; implement secure token storage, session expiry/re-authentication and sign-out.
6. Complete authenticated, user-owned domain API endpoints and database migrations before enabling cross-device sync.
7. Implement offline write queue, retry/idempotency, sync conflict policy, account isolation and sign-out data handling.
8. Add reminders/notifications with permission-denied and rescheduling behavior.
9. Replace basic Insights counters with honest date-range history/trends based on stored completion records; never infer missing history.
10. Complete the Coach action loop and clearly distinguish local guidance from remote AI capabilities.
11. Run accessibility, large-font, light/dark, compact-device, keyboard, offline, process-restart and account-switch checks on both platforms.
12. Capture side-by-side Android/iOS screenshots for all five destinations and all important empty/error/edit states against the approved Strata direction.

## Definition of done

Phase 1 is complete only when both native PRs are conflict-free, all CI checks pass on their latest heads, migration and persistence tests pass, authentication and account isolation are verified end-to-end, offline behavior is tested, the five core destinations meet acceptance criteria, and no unresolved release-blocking defects remain.

Subscriptions and billing remain outside Phase 1.
