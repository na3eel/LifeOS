# LifeOS Tirni Theme — Approved Visual Direction

**Status:** Approved visual direction / source of truth  
**Applies to:** Android (Jetpack Compose) and iOS (SwiftUI)  
**Scope:** Shared product theme and component styling. This document supersedes the previous Strata palette.

> The approved Tirni palette is inspired by the red brand reference supplied in the design review. It defines the product colour system; it does not change the LifeOS product name, logo, information architecture, or screen composition.

## Design intent

LifeOS should feel bold, confident, modern, and energetic without becoming visually overwhelming. Use crisp white page backgrounds, very light blush surfaces, near-black typography, and a focused crimson action colour. Keep the interface clean and readable.

## Core palette

| Token | Value | Usage |
|---|---|---|
| Primary | `#E5093B` | Primary actions, selected states, key progress, active navigation |
| Primary Light | `#FF3D6A` | Secondary emphasis, active highlights and small progress accents |
| Primary Dark | `#8B001C` | Pressed/strong states and restrained emphasis |
| Surface | `#FFF3F5` | Cards, grouped content, subtle input surfaces |
| Surface Soft | `#FFE1E8` | Selected chips, soft emphasis and progress tracks |
| Background | `#FFFFFF` | Main light-mode canvas |
| Border | `#F1DDE2` | Dividers, outlines, subtle separation |
| Text | `#111827` | Primary text and high-contrast icons |
| Secondary Text | `#6B7280` | Supporting labels and metadata |

## Colour usage rules

- Crimson is the dominant action colour. Use it for primary buttons, selected navigation, completion controls and meaningful progress.
- Use the bright red sparingly for highlights; deep crimson is for pressed or strong states.
- White is the default light canvas. Blush surfaces distinguish cards and grouped content without creating competing colour blocks.
- Do not use purple as a product accent in this theme.
- Use semantic status colours only when meaning requires them (success, warning, error); do not use them as decoration.
- Avoid gradients, glassmorphism, neon, glossy effects and heavy shadows. The supplied brand artwork may retain its own red tonal variation as an image asset.
- Do not flood entire screens with primary red. Keep the visual balance predominantly white with crimson actions.

## Typography

- Use **SF Pro** on iOS and the native system sans-serif / **Inter**-style hierarchy on Android.
- Keep a clear hierarchy: screen title, section title, body, supporting label, and numeric emphasis.
- Prefer readable sentence case. Maintain native dynamic type/font scaling and accessibility sizing.

## Shape, spacing and elevation

- Use comfortably rounded cards, fields, chips and buttons with a consistent radius scale.
- Prefer generous internal padding and clear separation between sections.
- Prefer subtle borders and low elevation. Elevation should communicate hierarchy, not decoration.
- Primary buttons should be visually obvious, full-width where the flow calls for it, and have clear pressed/disabled states.

## Shared component language

Use equivalent native components on both platforms for app bars, navigation, buttons, cards, grouped rows, fields, chips, progress indicators, empty/loading/error states, sheets and dialogs. Preserve native accessibility, navigation, keyboard and system behaviours.

## Screen direction

- **Today:** white canvas, blush cards, clear daily context, one prominent progress summary, focused action cards, scannable habit rows and crimson completion affordances.
- **Add Habit:** guided native sheet with searchable templates, optional custom entry, note, life-area assignment and one clear primary save action.
- **Areas:** calm, scannable area cards with restrained icon accents and meaningful progress.
- **Insights:** readable time filters, clear chart hierarchy and concise progress summaries.
- **Coach:** one clear conversational entry point and simple actionable prompts.
- **Calendar:** consistent date selection and completion states; never imply history that is not stored.

## Interaction and accessibility requirements

- Every interactive control needs visible default, pressed, focused, selected, disabled and error states as applicable.
- Preserve minimum touch targets (at least 44 pt iOS / 48 dp Android).
- Support screen readers, logical focus order, dynamic type, reduced motion and light/dark appearance.
- Do not rely on colour alone to communicate completion, selection or status.
- Validate contrast for text, icons and controls in both appearances. Use white text only on sufficiently dark crimson; use near-black text on pale blush surfaces.
- Keep sensitive habit titles out of lock-screen notification previews by default.

## Implementation guardrails

1. Define shared semantic tokens separately for Android and iOS, mapped to this document.
2. Use tokens and shared components; do not hard-code one-off colours, radii or shadows inside feature screens.
3. Any proposed deviation must be recorded here or in a linked design decision before implementation.
4. When a screen is redesigned, update both platforms and validate the same states on device/simulator.
5. Review the full app against this theme before declaring a screen complete.

## Acceptance checklist

- [ ] Both platforms use the same semantic palette and visual hierarchy.
- [ ] No unapproved accent colours or ad-hoc gradients/glass effects.
- [ ] Cards, fields, chips, buttons and navigation use shared tokenized styles.
- [ ] Add Habit supports clear new/edit states and keyboard-safe scrolling.
- [ ] Light and dark appearance are intentional and contrast-checked.
- [ ] Dynamic type/font scaling and screen-reader labels are verified.
- [ ] Screenshots from both platforms are reviewed side-by-side before sign-off.
