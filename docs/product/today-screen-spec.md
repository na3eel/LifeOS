# LifeOS Today Screen — Product and UX Specification

Status: implementation target; not a release sign-off.

## Purpose
The Today screen is the user's daily command centre. It should answer, in order:
1. What day is it and what matters?
2. What is my progress today?
3. What is the single next action?
4. Which habits are due today?
5. What is coming up next?

## Information architecture
- Header: local weekday/date, greeting, notification and profile actions.
- Day navigator: horizontally scrollable seven-day strip, selected date, previous/next week controls, accessible selected/today states.
- Progress summary: completed due habits / due habits; completed tasks / due tasks. Empty state is explanatory, never fabricated.
- Focus: one user-selected priority when configured; otherwise first incomplete task is a suggested focus, clearly labelled as a suggestion. Starting focus opens a timer, it must not silently mark a task complete.
- Habits: only habits scheduled for selected local date. Completion is idempotent by (user, habit, local date); allow undo and show completion state.
- Upcoming: tasks/events sorted by local start/due time; no placeholder events.
- Quick add: habit, task, goal and event actions. Preserve form input if validation or permission flow interrupts.

## Preset habit library
Presets are opt-in templates, never seeded as active habits. Initial collection:
- Wake up at 05:00 (time target)
- Gym / Workout (completion or duration)
- Avoid pornography (private yes/no; hidden from lock-screen notifications by default)
- Reading / Learning (duration)
- Budget tracking (completion)
- Project work (duration)
- No alcohol (private yes/no; neutral language)
- Social media detox (duration/limit)
- Goal journaling (completion)
- Cold shower (completion)

Every preset can be renamed, reassigned to a life area, rescheduled, have its target changed, have reminders configured, or be deleted. Presets must be searchable and grouped by category. Avoid moralising copy. Private/sensitive habit names must not be exposed in notification previews by default.

## Scheduling and date semantics
- Persist recurring weekday selection and local wall-clock reminder time, with an IANA timezone identifier.
- Store completion against a local calendar date and user timezone; never derive a habit day from UTC date alone.
- Define DST behaviour: preserve local wall-clock reminder time; if a local time is skipped, schedule at the next valid instant; if repeated, deliver at most once per occurrence.
- Changing selected date is read-only history navigation; completion on a past date requires an explicit action and clear confirmation.
- A week strip must handle month/year boundaries and locale first-day-of-week.

## Focus timer
- Start, pause, resume, finish and discard.
- Persist start timestamp, accumulated paused duration and session status so process death does not reset elapsed time.
- Use monotonic time while process is alive and wall-clock reconciliation after restart.
- Finishing records actual elapsed focused duration; never auto-complete the linked task.
- Prevent duplicate sessions from rapid taps.

## Empty, loading, offline and error states
- New account: explain how to add first habit/task; no fake activity or fabricated percentages.
- No habits due today: distinguish from no habits created.
- Offline: allow local changes, queue sync, show unobtrusive sync state and retry.
- Failed save: preserve user input and expose retry; do not display raw exception text.
- Loading: skeleton only where layout stability benefits; avoid global blocking spinners.

## Accessibility and platform quality
- Minimum target 48dp Android / 44pt iOS.
- Screen-reader names and logical traversal order for date selector, progress, habit toggles and quick actions.
- Dynamic Type/font scaling without clipping; support large text and display zoom.
- Contrast checked in light and dark palettes; colour is never the only completion signal.
- Reduced-motion setting respected.
- Keyboard and sheets must not cover primary actions; form scrolls to focused field.
- Confirm destructive actions; support undo for habit completion.
- All state changes are account-scoped and durable.

## Acceptance tests
- First-run empty state contains no fake counts or events.
- Selecting a date loads only habits due for that local date.
- Week navigation crosses month/year boundaries and respects locale.
- Completing a habit twice creates one completion; undo removes/voids the completion safely.
- Preset selection creates an editable user-owned habit only after explicit save.
- Reminder permissions denied: habit creation still succeeds and settings explain how to enable later.
- Reminder fires at local wall-clock time across timezone/DST changes and at most once per occurrence.
- Focus timer survives backgrounding and process restart without double-counting paused time.
- Task completion and focus-session completion remain separate actions.
- Offline create/update/complete actions survive restart and sync without duplicates.
- Light/dark, large text, screen reader and reduced motion checks pass on both platforms.
