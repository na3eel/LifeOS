# Phase 1 Native Development — Working Status

Last updated: 2026-11-03

This is a working hand-off checklist, not a release sign-off. Phase 1 is **not complete** until native workflows, automated tests, physical-device checks and release gates pass.

## Scope and exclusions

Phase 1 targets installable native Android and iOS apps with Today, Areas, Calendar/Plan, Insights and Coach; secure registration/sign-in; habits, life areas, goals, tasks, accurate insights, reminders, durable local data and reliable offline behavior.

Subscriptions, RevenueCat and billing are explicitly deferred to the final monetisation phase.

## Recent implementation
- [x] iOS app tab now uses the canonical Today feature view instead of a duplicate simplified screen.
- [x] iOS Today has seven-day date navigation, week navigation controls and date-scoped completion display.
- [x] Approved Strata palette is defined in the iOS root theme.
- [ ] Verify iOS simulator build and unit tests for the latest commit.
- [ ] Install and inspect on a physical iPhone.

## Remaining Phase 1 gates

### Shared product and design
- [ ] Complete approved Strata hierarchy on both platforms: header, date navigator, progress, focus, habits, upcoming and quick actions.
- [ ] Finish calendar/history navigation and date-aware task scheduling.
- [ ] Add durable focus timer (start/pause/resume/finish/discard); finishing a timer must never auto-complete a task.
- [ ] Add empty/loading/offline/retry/error states without fabricated activity.
- [ ] Complete large-text, screen-reader, contrast, reduced-motion and touch-target checks.

### iOS
- [ ] Complete all five screen workflows and SwiftData persistence tests.
- [ ] Integrate registration, sign-in, email verification, password reset, session restore and sign-out with the backend.
- [ ] Preserve Keychain credentials on transient network failures; clear only invalid/unauthorized sessions.
- [ ] Add native reminders and permission-denied behavior.
- [ ] Implement offline changes and conflict-safe sync.
- [ ] Verify SwiftData migration and process-restart behavior.
- [ ] Physical-device QA on iPhone.

### Android
- [ ] Complete all five screen workflows and Room persistence tests.
- [ ] Integrate registration, sign-in, email verification, password reset, session restore and sign-out.
- [ ] Replace plaintext token storage with encrypted storage backed by Android Keystore.
- [ ] Add native reminders and permission-denied behavior.
- [ ] Implement durable offline command queue and conflict-safe sync.
- [ ] Verify Room migrations and inspect launcher icon on a physical Android device.

### Backend and integration
- [ ] Verify auth contracts and API error mapping against both clients.
- [ ] Add integration tests for registration, email verification, login, lockout, password reset and authenticated profile.
- [ ] Add authenticated user-scoped data endpoints for habits, completions, life areas, tasks, goals and insights.
- [ ] Add sync protocol/idempotency and timezone-safe scheduling contracts.
- [ ] Verify production HTTPS, email delivery and secrets handling.

## Branches
- Android: `work/android-native-compose` — PR #3 (draft; do not merge before release gates pass).
- iOS: `work/ios-native-swiftui` — PR #4 (draft; do not merge before release gates pass).
- Backend: `work/backend-foundation` — PR #2 (draft; do not merge before release gates pass).

## Latest iOS commits
- Today root wiring: `e10bdf4b23463b8f17c9cb1164f4028f5b95268c`
- Today date navigation: `31da124b7da37a97e8dab867c7e634d52c76048d`

Only mark a build/test as passing when the exact commit has a completed successful CI run. CI alone does not complete Phase 1; physical-device and acceptance testing are required.
