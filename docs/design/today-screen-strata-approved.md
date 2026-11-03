# LifeOS Today — Approved Strata Design Reference

**Design status:** Approved direction; canonical implementation reference  
**Applies to:** Native Android (Jetpack Compose) and native iOS (SwiftUI)  
**Product:** LifeOS — Build better days. Build a better life.

This document records the approved Strata direction and is the design guardrail for all Today-screen implementation work. It supplements `docs/product/today-screen-spec.md`, which defines behaviour and acceptance criteria.

## Approval and change control

The approved direction is **Strata**. Do not replace it with Apple-style, bold/experimental, glassmorphism, gradient-heavy, or another visual direction. Do not redesign the screen independently on either platform. Preserve the same information hierarchy and visual character across Android and iOS while respecting native platform conventions.

Any proposed change to the Today screen's composition or visual language must be reviewed against this reference and explicitly approved before implementation.

## Visual language

- Calm, premium, editorial and structured; visually quiet rather than flashy.
- Warm/off-white page and card surfaces with near-black primary typography.
- Orange is the primary action and progress colour: `#DB5635`.
- Purple is a restrained secondary accent only: `#9828F6`.
- White/warm-white: `#FFFEFC`; near-black: `#171313`.
- No purple gradients, glass effects, decorative glow, excessive shadows, or gratuitous colour blocks.
- Clear typographic hierarchy, deliberate whitespace, compact but comfortable cards, consistent alignment and restrained corner radii.
- Use native controls and platform accessibility conventions. Do not use emoji as interface icons.

## Approved Today-screen composition

Keep this order and hierarchy unless a change is explicitly approved:

1. **Header** — local date/day context and a confident, concise greeting; notification/profile actions remain secondary.
2. **Week navigator** — a horizontal seven-day date strip with a clear selected day and distinct Today state. Previous/next week navigation must remain available.
3. **Daily progress** — a compact, prominent summary of real progress for the selected date. Orange carries the primary progress signal; supporting metrics remain visually subordinate. Never fabricate counts or percentages.
4. **Focus** — one clear next action / focus task, visually prominent without overwhelming the screen. Starting focus starts a timer; it must not silently complete a task.
5. **Habits** — a clean, scannable list of habits due on the selected date, with large, accessible completion controls and concise supporting metadata. Preset habits are editable user-owned entries, not fixed UI content.
6. **Upcoming** — a restrained chronological view of real tasks/events, clearly separated from habits.
7. **Quick actions** — add habit/task and related actions are easy to find but do not compete with the primary daily content.

The page scrolls naturally on smaller phones. Preserve the same hierarchy on both platforms; adapt only interaction details that need to feel native.

## Interaction and content guardrails

- Selected date changes the displayed day and its real completion/history data.
- Habit completion is date-specific, reversible and durable.
- Preset habits are opt-in and editable before/after saving; never auto-activate all presets.
- Focus timer state is durable and independent of task completion.
- Empty, loading, offline and error states must retain the same calm visual language and must not invent activity.
- Sensitive/private habits must not be exposed in lock-screen notification previews by default.
- Meet Android 48dp and iOS 44pt minimum touch targets; support large text, screen readers, dark mode and reduced motion.

## Implementation rule

Treat this file as the **approved visual direction**, and `docs/product/today-screen-spec.md` as the **functional contract**. Before changing either native Today screen:

1. Read both documents.
2. Compare the implementation with the approved Strata composition above.
3. Keep visual changes within this direction; do not introduce a new concept.
4. Verify Android and iOS independently and record any platform-specific deviations.

## Original mockup asset

The original approved visual mockup image is not currently checked into this repository. This document records the approved direction and layout from the design review, but it is **not a pixel-exact substitute for the original mockup**. Add the original exported image at `docs/design/references/today-screen-strata-approved.png` when it is available, then link it here. Do not recreate or claim a replacement image is the approved original.
