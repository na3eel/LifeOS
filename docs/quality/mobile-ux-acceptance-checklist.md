# Mobile UX & Reliability Acceptance Checklist

This checklist is a release gate for every LifeOS Android and iOS screen and flow. Apply it during design, implementation, code review, and device testing. A screen is not complete merely because it builds.

## Visual design and layout

- [ ] **No purple gradients.** Do not use generic purple/blue gradient backgrounds, gradient cards, or gradient buttons. Use the approved restrained LifeOS design tokens and solid surfaces.
- [ ] **No excessive padding.** Use a consistent spacing scale. Keep content density intentional, avoid oversized empty regions, and verify layouts on compact and large devices.
- [ ] **No emoji in the product UI.** Use the approved icon system or native vector/SF Symbols assets. Do not use emoji as icons, status indicators, or decoration.
- [ ] **Buttons and controls are not too small.** Meet platform accessibility touch-target guidance (at least 48dp on Android and 44pt on iOS where practical); provide adequate spacing between adjacent targets.
- [ ] **Text is comfortably readable.** Avoid tiny labels and low-contrast secondary text. Use semantic typography styles rather than arbitrary small sizes.
- [ ] **No visual clutter from spinners.** Show loading indicators only for meaningful work, in the relevant region. Prefer skeletons or stable placeholders for content loading; never show multiple redundant spinners.
- [ ] **Light and dark themes both work.** All screens, dialogs, fields, charts, icons, dividers, system bars, and loading/error/empty states must use semantic theme tokens and remain legible in both modes.
- [ ] **Text scaling and accessibility settings work.** Respect Android font scaling and iOS Dynamic Type. Support larger text without clipping, overlap, truncation of essential actions, or broken layouts.

## Forms, keyboard, and interaction

- [ ] **The keyboard never hides the active input or primary action.** Handle IME/safe-area insets, scroll focused fields into view, and keep submit/continue actions reachable. Test multi-field forms and smaller screens.
- [ ] **Taps provide clear feedback.** Buttons and tappable rows have appropriate pressed/selected states and restrained, purposeful animations. Respect Reduce Motion / system animation preferences.
- [ ] **No duplicate submissions.** Disable or guard the submit action while a request is in flight; make mutation endpoints idempotent where appropriate. Rapid double taps, retries, and network delays must not create duplicate records or payments.
- [ ] **Focus and navigation are predictable.** Set sensible focus order, keyboard actions, back behavior, and focus dismissal. Do not unexpectedly clear user-entered data when navigating.

## Data, loading, errors, and first-run experience

- [ ] **New users never see a blank screen.** Every first-run, empty, loading, permission-denied, and failure state has a useful explanation and a clear next action. Seed no fake user data.
- [ ] **Caching and offline behavior are intentional.** Cache suitable read data, show freshness when relevant, persist user changes locally, and reconcile safely when connectivity returns. Avoid stale or cross-account data.
- [ ] **Errors are never raw or technical.** Do not expose stack traces, exception messages, SQL/API details, or internal identifiers. Show a plain-language message, preserve entered data, and provide a safe retry or recovery action.
- [ ] **User preferences are not wiped.** Persist theme choice, onboarding progress, notification preferences, and other settings appropriately across app restarts and upgrades. Clear account-specific data only on an explicit sign-out/account-removal flow, with correct multi-account isolation.
- [ ] **Loading, success, empty, and error states are consistent.** Every asynchronous operation has a defined state and avoids flicker, indefinite loading, or silent failure.
- [ ] **Network retries do not corrupt data.** Handle timeouts, offline transitions, token expiry, and server conflicts without losing user input or applying a mutation twice.

## Release verification

- [ ] Test on compact and large phones, current supported OS versions, light and dark mode, and increased text sizes.
- [ ] Test keyboard behavior on sign-in, onboarding, search, habit/task creation, editing, and any multi-field form.
- [ ] Test rapid repeated taps, slow network, offline mode, app background/foreground, process termination, and relaunch.
- [ ] Verify accessibility labels, screen-reader order, contrast, touch targets, and reduced-motion behavior.
- [ ] Confirm no user-facing screen contains placeholder content, raw errors, emoji, or unapproved gradient styling.

## Enforcement

Treat every unchecked applicable item as a release-blocking defect. Record intentional platform-specific exceptions in the relevant pull request with a reason and a test result. Run this checklist for both native implementations independently:

- Android: Kotlin + Jetpack Compose.
- iOS: Swift + SwiftUI.

The checklist applies to all current and future LifeOS features, not only the Today screen.
