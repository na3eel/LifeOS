# LifeOS Strata Theme — Approved Visual Direction

**Status:** Approved source of truth  
**Applies to:** Android (Jetpack Compose) and iOS (SwiftUI)

## Design intent

Calm, premium, editorial and structured. The interface should feel warm, focused and visually quiet rather than flashy. Use warm off-white canvases, soft beige cards, near-black typography, orange primary actions and a restrained purple secondary accent.

## Core light palette

| Token | Value | Usage |
|---|---|---|
| Canvas | `#FFFBF7` | Main page background |
| Card / surface | `#F3E8E1` | Cards and grouped content |
| Surface muted | `#E9DDD6` | Secondary surfaces |
| Border | `#D6C5BC` | Dividers and outlines |
| Primary text | `#1F1724` | Main text and icons |
| Secondary text | `#6F626B` | Supporting labels |
| Primary orange | `#DB5635` | Primary actions and progress |
| Deep orange | `#B83F25` | Strong/pressed orange states |
| Orange soft | `#FCE4DA` | Selected and highlighted surfaces |
| Secondary purple | `#9828F6` | Restrained secondary accent |
| Purple soft | `#F0E4FA` | Secondary selected surfaces |

## Colour rules

- Warm off-white is the default page canvas; soft beige distinguishes cards.
- Orange is the primary action and progress colour.
- Purple is a restrained secondary accent only; never compete with orange.
- Use near-black for primary text and muted grey-purple for supporting text.
- Avoid gradients, glass effects, glow, glossy effects, heavy shadows and gratuitous colour blocks.
- Do not use large saturated colour blocks as card backgrounds.

## Typography, shape and spacing

Use native system typography with a clear screen-title, section-title, body and supporting-label hierarchy. Preserve dynamic type/font scaling. Use consistent rounded cards and controls, generous whitespace, subtle borders and low elevation.

## Screen direction

Today follows the approved hierarchy: Header, Week navigator, Daily progress, Focus, Habits, Upcoming and Quick actions. Keep the same hierarchy on both platforms while respecting native conventions.

## Appearance

Strata light mode is the default app appearance. Dark palette tokens remain defined for a future explicit in-app appearance preference; the operating-system dark setting must not unexpectedly switch the app away from the approved light design.

## Accessibility and implementation

- Preserve minimum touch targets (44 pt iOS / 48 dp Android).
- Support screen readers, logical focus order, dynamic type and reduced motion.
- Validate contrast for text, icons and controls.
- Use semantic tokens, not one-off hardcoded colours in feature screens.
- Any visual direction change requires explicit approval and an update to this document.
