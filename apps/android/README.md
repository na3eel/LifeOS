# LifeOS Android

Native Android application built with Kotlin, Jetpack Compose and Material 3. No React Native, Expo, WebView or cross-platform UI runtime.

## Current foundation
- Native Activity and Compose entry point
- System light/dark theme
- Five-destination bottom navigation
- Today dashboard with interactive habit check-offs
- Adaptive layout foundation for phones/tablets to follow

## Lumen UI
Lumen native Compose components are the design-system source of truth for shared primitives. The Lumen catalog currently documents `com.santi020k:lumen-compose` as a source checkout integration: pin a Lumen release, include `packages/compose` in the Gradle build, and use `LumenTheme`, `LumenCard`, `LumenText`, and `LumenNavigationBar`. Do not replace this with the React/DOM Lumen package.

The current screen uses Material 3 directly as a compiling native scaffold while the exact Lumen source release is pinned and wired into the build. This is an intentional integration boundary, not a claim that Lumen is already linked.

## Build
Open `apps/android` in Android Studio with JDK 17+ and Android SDK 36.
