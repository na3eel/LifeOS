# Phase 1 Native Development — Working Status

Last updated: 2026-11-03

This is the hand-off checklist for Phase 1. It is deliberately a status document, not a release sign-off. Phase 1 is **not complete** until all required platform workflows, tests, device checks and release gates are satisfied.

## Scope and exclusions

Phase 1 targets installable native Android and iOS apps with the five product areas: Today, Areas, Calendar/Plan, Insights and Coach; secure registration/sign-in; habits, life areas, goals, tasks, accurate insights, reminders, durable local data, and reliable offline behavior.

Subscriptions, RevenueCat and billing are explicitly deferred to the final monetisation phase.

## Recent implementation

- [x] Android launcher manifest now points to LifeOS launcher resources; adaptive and legacy launcher resources added.
- [x] Android Material 3 color scheme now receives the approved Strata tokens as well as the Lumen theme.
- [x] Android Today has a seven-day navigator, previous/next week controls and date-scoped habit completion display.
- [x] iOS app tab now uses the canonical Today feature view rather than a separate duplicate screen.
- [x] iOS Today has a seven-day navigator and date-scoped habit completion display.
- [ ] Verify Android build and Room instrumentation tests for the latest native commits.
- [ ] Verify iOS simulator build and unit tests for the latest native commits.

## Remaining Phase 1 gates

### Shared product and design
- [ ] Complete approved Strata screen hierarchy on both platforms (header, date navigator, progress, focus, habits, upcoming, quick actions).
- [ ] Finish calendar/history navigation and date-aware task scheduling.
- [ ] Add durable focus timer with start/pause/resume/finish/discard; timer completion must never auto-complete a task.
- [ ] Add empty, loading, offline, retry and error states without fabricated activity.
- [ ] Complete large-text, screen-reader, contrast, reduced-motion and touch-target checks.

### Android
- [ ] Complete all five screen workflows and their persistence tests.
- [ ] Integrate registration, sign-in, email verification, password reset, session restore and sign-out with the backend.
- [ ] Replace plaintext token storage with encrypted storage backed by Android Keystore.
- [ ] Add native reminders and permission-denied behavior.
- [ ] Implement durable offline command queue and conflict-safe sync.
- [ ] Verify Room migrations from every supported prior schema.
- [ ] Install and inspect the generated APK on a physical Android device, including launcher icon.

### iOS
- [ ] Complete all five screen workflows and SwiftData persistence tests.
- [ ] Integrate registration, sign-in, email verification, password reset, session restore and sign-out with the backend.
- [ ] Preserve Keychain credentials on transient network failures; clear only invalid/unauthorized sessions.
- [ ] Add native reminders and permission-denied behavior.
- [ ] Implement offline changes and conflict-safe sync.
- [ ] Verify SwiftData migration and process-restart behavior.
- [ ] Install and inspect on a physical iPhone.

### Backend and integration
- [ ] Verify auth contracts and API error mapping against both native clients.
- [ ] Add integration tests for registration, email verification, login, lockout, password reset and authenticated profile.
- [ ] Add authenticated, user-scoped data endpoints for habits, completions, life areas, tasks, goals and insights.
- [ ] Add sync protocol/idempotency and timezone-safe scheduling contracts.
- [ ] Verify production configuration, HTTPS, email delivery and secrets handling.

## Branches and review

- Android: `work/android-native-compose` — PR #3 (draft; do not merge until release gates pass).
- iOS: `work/ios-native-swiftui` — PR #4 (draft; do not merge until release gates pass).
- Backend: `work/backend-foundation` — PR #2 (draft; do not merge until release gates pass).

## Latest native commits at hand-off

- Android theme: `bfd98c593885770745a72fdb2a81e3dbf9445d07`
- Android Today date navigation: `51160c4ee26e4cecd07fb226a7e3aa0a60ae54a7`
- iOS Today root wiring: `e10bdf4b23463b8f17c9cb1164f4028f5b95268c`
- iOS Today date navigation: `31da124b7da37a97e8dab867c7e634d52c76048d`

Do not mark any build or test as passing unless the exact latest commit has a completed successful CI run. Do not mark Phase 1 complete based on CI alone; physical-device and acceptance testing are also required.
