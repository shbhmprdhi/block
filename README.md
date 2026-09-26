# BLOCK

An offline-first Android companion for the supplied training and meal plans.

## Build

- Android application: Kotlin + Jetpack Compose Material 3 Expressive.
- Android Gradle Plugin 9.1.1, Gradle 9.3.1, JDK 17.
- Compile SDK 37, target SDK 36 (Android 16), minimum SDK 26.
- Bundled Google Sans Flex variable font; `ROND=100` throughout app typography.
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`.
- APK target: arm64-v8a phone. The app has no native-code dependencies.

GitHub Actions runs `assembleDebug` on pushes, pull requests, and manual dispatch, then uploads the APK as a 14-day artifact. The workflow uses maintained GitHub Actions releases, validates the Gradle wrapper through Gradle's setup action, and caches dependencies with its basic cache provider.

## App behavior

- Bottom dock: Today · BLOCK · FUEL · Progress.
- BLOCK uses monochrome workout surfaces with restrained accent UI and typography-only workout headers. FUEL and the other overview screens use a colorful fixed Material 3 Expressive palette.
- The app is named BLOCK. All content is seeded from `gym_planv1.txt` and `diet.txt`; screenshot content is not imported.
- Workout schedule follows weekdays. Missed workouts stay on their calendar day and are not moved automatically.
- Week 1–2 uses 2 sets per exercise at RIR 3–4; week 3–4 uses 3 sets at RIR 2–3; afterward the source exercise prescriptions apply. The program start date can be changed in Settings.
- Creatine is one 5 g breakfast check-in. Hydration follows the plan's check-in prompts; the app does not infer intake from meals.
- Exercise targets, cues, meal names, and meal details are editable in the Edit Plan screen. Edits, check-ins, logs, and progress entries persist locally.
- Set logging supports load, reps/distance, RIR, prior-value suggestions, rest timer, optional haptics, optional timer tone, and optional system-voice exercise cues.
- Android cloud backup and device-transfer backup are disabled. There is no account, network permission, or sync service.

## Build notes

- Material 3 Expressive uses the current `1.5.0-alpha29` theme and motion APIs; dependency versions live in `gradle/libs.versions.toml`.
- Gradle wrapper and distribution checksums are pinned. No release signing credentials are stored in this repository.
- Font license is included at `licenses/GoogleSansFlex-OFL.txt`.

