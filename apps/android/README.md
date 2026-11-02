# LifeOS Android

Native Android application built with Kotlin, Jetpack Compose and Material 3. The application uses Room for durable on-device storage.

## Phase 1 foundation
- Native Kotlin + Jetpack Compose application
- Room database with Habit, Area, Task and Goal entities and Flow-backed DAO queries
- Today: daily habit progress, create/complete/delete habits, and today's task list
- Areas: seeded starter areas (Body, Mind, Soul, Social, Career), habit counts and goal progress
- Plan: create, complete and delete tasks
- Insights: live counts derived from locally stored records
- Coach: on-device contextual prompts; no false claim of a connected AI service
- System light/dark theme and native bottom navigation
- Empty states instead of fabricated sample habits

## Persistence
Room database name: `lifeos.db`. Data is local to the installation. Cloud account sync and cross-device conflict resolution are not implemented in this phase.

## Build
Open `apps/android` in Android Studio with JDK 17+ and Android SDK 36. CI builds the debug APK and uploads it as a 30-day artifact.

## Design system
Material 3 is currently used for the native compiling foundation. Lumen integration remains a separate follow-up; this README does not claim the Lumen library is already linked.
