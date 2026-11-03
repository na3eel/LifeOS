# LifeOS Strata Theme — Approved Visual Direction

**Status:** Approved visual direction / source of truth  
**Applies to:** Android (Jetpack Compose) and iOS (SwiftUI)  
**Scope:** Shared product theme and component styling. This document supersedes ad-hoc screen-level styling decisions.

> The approved preview is a visual direction reference, not a pixel-perfect implementation spec. Implement the tokens and interaction rules below natively on each platform.

## Design intent

LifeOS should feel **modern, warm, focused, and purposeful**. The interface should reduce visual noise and make daily actions feel approachable. Keep the same visual identity across Today, Add Habit, Areas, Calendar, Insights, and Coach.

## Core palette

| Token | Value | Usage |
|---|---|---|
| Primary | `#DB5635` | Primary actions, selected states, key progress, active navigation |
| Secondary | `#9828F6` | Secondary emphasis, selected supporting accents |
| Background | `#FFFBF7` | Main light-mode canvas; warm white |
| Surface | `#F3E8E1` | Cards, grouped content, subtle input surfaces |
| Border | `#D6C5BC` | Dividers, outlines, subtle separation |
| Text | `#1F1724` | Primary text and high-contrast icons |

Use semantic tokens (e.g. `colorPrimary`, `colorSurface`) rather than scattering hex values through views. Validate text and control contrast in both themes.

### Color usage rules

- Orange is the dominant action color; do not introduce competing accent colors.
- Purple is a restrained secondary accent, not a second primary.
- Warm white is the default light canvas. Avoid stark pure-white page backgrounds where the warm canvas is intended.
- Use semantic status colors only when meaning requires them (success, warning, error); do not use them as decoration.
- Gradients may appear only as restrained, optional illustration/hero artwork. Never use gradients as a substitute for core surfaces or controls.
- No glassmorphism, neon, heavy drop shadows, or glossy effects.

## Typography

- Use **SF Pro** on iOS and the native system sans-serif / **Inter**-style hierarchy on Android.
- Keep a clear hierarchy: screen title, section title, body, supporting label, and numeric emphasis.
- Prefer readable sentence case. Avoid overly dense uppercase labels.
- Maintain platform-native dynamic type/font scaling and accessibility sizing.

## Shape, spacing, and elevation

- Use comfortably rounded cards, fields, chips, and buttons with a consistent radius scale.
- Prefer generous internal padding and clear separation between sections.
- Use subtle borders and low elevation. Elevation should communicate hierarchy, not decoration.
- Primary buttons should be visually obvious, full-width where the flow calls for it, and have clear pressed/disabled states.
- Chips and segmented controls must communicate selected, unselected, and disabled states clearly.

## Shared component language

Implement equivalent native components on both platforms for:
- App bars and screen headers
- Bottom navigation and selected navigation state
- Primary, secondary, and text buttons
- Cards and grouped list rows
- Text fields, search fields, and multiline notes
- Chips, category filters, and segmented controls
- Progress rings/bars and stat summaries
- Empty, loading, error, and success states
- Sheets/dialogs and confirmation patterns

Do not force identical platform mechanics: preserve native accessibility, navigation, keyboard, sheet, and system behaviors while keeping the same visual language.

## Screen direction

- **Today:** warm canvas, clear daily context, one prominent progress summary, focused action cards, scannable habit rows, restrained orange completion affordances.
- **Add Habit:** guided native sheet; searchable templates, optional custom entry, note, life-area assignment, and one clear primary save action. New and edit modes must be unmistakable.
- **Areas:** calm, scannable area cards with distinct but restrained icon accents and meaningful progress.
- **Insights:** readable time filters, clear chart hierarchy, and concise progress summaries; avoid decorative chart effects.
- **Coach:** one clear conversational entry point and simple actionable prompts; avoid clutter.
- **Calendar:** consistent date selection and completion states; never imply history that is not stored.

## Interaction and accessibility requirements

- Every interactive control needs visible default, pressed, focused, selected, disabled, and error states as applicable.
- Preserve minimum touch target guidance (at least 44 pt iOS / 48 dp Android).
- Support screen readers with meaningful labels and logical focus order.
- Respect system font scaling, reduced motion, and light/dark appearance settings.
- Do not rely on color alone to communicate completion, selection, or status.
- Keep motion subtle, purposeful, and interruptible.
- Avoid exposing sensitive habit titles in lock-screen previews by default.

## Implementation guardrails

1. Define shared design tokens separately for Android and iOS, with values mapped to this document.
2. Use tokens and shared components; do not hard-code one-off colors, radii, or shadows inside feature screens.
3. Any proposed deviation must be recorded here or in a linked design decision before implementation.
4. When a screen is redesigned, update both platforms and validate the same states on device/simulator.
5. Review the full app against this theme before declaring a screen complete. A recolored screen alone is not a completed redesign.

## Acceptance checklist

- [ ] Both platforms use the same semantic palette and visual hierarchy.
- [ ] No unapproved accent colors or ad-hoc gradients/glass effects.
- [ ] Cards, fields, chips, buttons, and navigation use shared tokenized styles.
- [ ] Add Habit supports clear new/edit states and keyboard-safe scrolling.
- [ ] Light and dark appearance are intentional and contrast-checked.
- [ ] Dynamic type/font scaling and screen-reader labels are verified.
- [ ] Screenshots from both platforms are reviewed side-by-side before sign-off.

## Reference preview

The approved concept preview shows the intended direction across Today, Add Habit, Areas, Insights, and Coach. Treat it as a mood/layout reference; the token table and guardrails in this document are authoritative.
