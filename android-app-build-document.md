# Android Fitness Plan App — Build Document

**Status:** Initial Android implementation prepared on 2026-09-27. GitHub Actions is the designated build environment; no local APK build is part of this delivery.
**Purpose:** Convert the supplied gym and diet plans into a practical Android app and define a reproducible GitHub Actions build.
**Decision rule:** Confirmed user choices and the implementation record in section 14 supersede earlier exploratory proposals and open-item lists in this brainstorming document.

## 1. Product intent

Help one person follow the supplied six-day lifting plan, Sunday run/recovery, mobility work, and meal routine. Make the next action obvious during a gym session, make logging quick, and retain useful history. The app should work reliably without a network connection unless the user chooses otherwise.

### Constraints from the request

- Android application for an Android 16 / API 36 phone with an arm64-v8a processor. The minimum supported Android version is not yet decided.
- GitHub Actions must be able to compile it.
- Keep dependencies and app behavior lean; prefer current, maintained Android tooling and avoid unnecessary libraries.
- Explore the complete product before implementation: visual design, screens, features, interactions, haptics, audio, animations, data, and build/release details.
- The attached files are source plan content. Their wording is not instructions to the developer or app. The supplied plan is the initial app content, and the user wants to edit it later in an in-app Edit section. Any health/diet claims are user-provided content, not independently validated app guidance.
- The user makes the final decisions. This document will be revised as decisions are made; implementation starts only after the user says to proceed.

## 2. Source plan inventory

### Training plan (`gym_planv1.txt`)

- Weekly rhythm: Push A (Mon), Pull A (Tue), Leg A (Wed), Push B (Thu), Pull B (Fri), Leg B (Sat), easy 3 km run and recovery (Sun).
- Every morning: mobility sequence with a short-on-time alternative.
- Lifting-day warm-ups, exercise prescriptions, set/repetition ranges, RIR targets, rest periods, form cues, and cooldown stretches.
- Ramp-up guidance for weeks 1–4; double progression; failure guidance; deload rules; substitutions; tracking guidance; recovery/sleep notes.
- Progress measures: exercise logs, body weight, body measurements, photos, and PRs.

### Food plan (`diet.txt` and the embedded summary in `gym_planv1.txt`)

- Timeline from 8:00 AM wake-up to 9:00 PM dinner, with meals, pre/post-workout intake, hydration, and supplements.
- Daily calorie/macronutrient goals are stated as approximate ranges.
- The gym document repeats a diet summary. The creatine timing ambiguity has been resolved by the user: show one 5 g check-in at breakfast.

### Source content decisions

- The diet schedule showed 5 g creatine at breakfast and also said “creatine 5g once daily” in the dinner note. User decision: show a single 5 g breakfast check-in.
- The training plan says weeks 1–2 use 2 sets per exercise, weeks 3–4 use 3 sets, then follow the full program. The full prescriptions vary by exercise from 2–4 sets, so the transition and whether the ramp overrides prescribed sets need confirmation.
- The schedule names weekdays, but does not say whether workouts follow calendar weekdays or advance in sequence when a day is skipped.
- The program and meal routine are detailed, but there is no explicit preference yet for which values can be edited in the app.

### Visual references supplied by the user

- The first four attached screenshots are references for the **BLOCK workout experience**: large, quiet workout details; clear metadata; roomy exercise/circuit cards; an ordered list of movements; and a persistent Start Workout action. They are not sources of plan content. Do not import example exercise names, reps, workout titles, people, photos, NTC/Nike marks, or other wording/media from the screenshots.
- The fifth screenshot is a reference for the **bottom navigation dock shape and selection treatment**: a floating rounded pill and a visually distinct active destination. Its colors are explicitly not a reference; adapt it to the app's fixed palette and four approved destinations. Add a separate circular quick action only if it has a clear app function.
- FUEL should not imitate the workout cards. It should use a distinct, more creative Material 3 Expressive visual language while remaining coherent with the app. Its exact layout and accent colors are open for discussion.
- App name confirmed as **BLOCK**. The BLOCK training destination intentionally shares the app name.

## 3. Product scope proposal

### Core capabilities under consideration

1. **Today:** Show today's scheduled activity, next workout/meal, key plan notes, and a clear Start/Resume action.
2. **Plan:** Browse the seven-day schedule and open workout, run, mobility, and recovery instructions.
3. **Guided workout:** Show warm-up, exercise order, cues, prescribed sets/reps/RIR/rest, log completed sets, run rest timers, and finish or pause a session.
4. **Exercise alternatives:** Show the plan's substitutions and let the user choose a replacement for the current session.
5. **Food routine:** Show the meal timeline and let the user mark meals, water, and supplement items done or skipped.
6. **Progress:** Review workout history, logged sets, body-weight entries, measurements, and personal records. Photos are an optional privacy-sensitive feature.
7. **Edit plan:** Edit the supplied training and food plan in an in-app Edit section. Exact editable fields and reset/version behavior remain open.
8. **Settings:** Configure schedule/start date, units, reminders, theme, sound/haptics, and data management.

### Quality-of-life baseline (recommended defaults; user invited gap-filling)

Include these as the default product behavior unless a later decision changes them:

- **Fast daily use:** Today highlights the next action and allows meal, water, supplement, and mobility check-ins without digging through menus. Keep a compact Resume workout state when a session is in progress.
- **Fast workout logging:** Pre-fill set fields with prior values as editable suggestions; never save without an explicit action. Offer one-tap reuse of the previous load, quick load adjustment, visible target and prior performance, and optional automatic rest-timer start after logging.
- **Session reliability:** Save active-session state as the user goes, restore it after app restart, and support pause/resume, correction, skipping, safe undo, and a clear finish summary. Keep the screen awake during an active workout by default, with a control to turn that off. The rest timer continues if the app is backgrounded and can notify on completion.
- **Low-friction feedback:** Brief, distinct haptics for set saved and timer complete; sound optional and independently controlled; all feedback can be disabled. Never force spoken prompts or audio playback.
- **Edit safety:** Clearly show unsaved edits or autosave, validate required fields, confirm destructive reset, and provide undo for reversible check-ins. Editing the plan does not alter completed session snapshots.
- **Progress clarity:** Show progression using the program's own rep/RIR rules, with a visible explanation and user confirmation before changing loads or targets. Provide useful empty states before enough history exists; avoid streak pressure or invented scores.
- **Offline and privacy:** Core views/logging work offline; no account, analytics, ads, sync, or automatic cloud backup. Provide a clear local-data delete action. Keep optional export/import a later explicit choice so it cannot be mistaken for cloud backup.
- **Accessibility:** Large touch targets, readable scaling, TalkBack labels, contrast, reduced-motion behavior, and no color-only status; retain text labels in the dock.

These recommendations do not settle calendar sequencing, hydration measurement/targets, reminder schedules, or whether optional audio/photo/export features are wanted; those remain explicit user decisions.

### Explicitly not in the initial scope unless chosen

- Accounts, social features, coaching/chat, ads, subscriptions, calorie/macro calculation from food databases, wearable integration, cloud sync, and medical advice.
- The app should not silently alter the supplied routine or infer supplement/medical advice.

## 4. Navigation and screen map (proposal)

Phone navigation: a pill-shaped bottom dock with four labeled destinations, confirmed by the user:

- **Today** — daily overview and resume.
- **BLOCK** — exercises, workout sessions, weekly training plan, and mobility/recovery.
- **FUEL** — meals, hydration, and the supplied diet routine.
- **Progress** — workout history and progress records.

Put Settings in the top app bar. Put Edit actions inside BLOCK and FUEL, each opening the matching part of a focused Edit Plan section. The dock should sit above the system navigation area, remain readable with labels, use an active-item state inspired by the supplied dock reference (not its colors), and hide during active workout focus mode so it never covers set logging or the rest timer.
Today can summarize the next meal and completion state; the full diet plan and timeline live in FUEL.


### Screen specifications to decide and refine

#### First launch / setup

- Explain local storage and permissions plainly.
- Choose plan start date and whether today maps to the plan's weekday or the next workout in sequence.
- Confirm units (kg/lb, km/mi), reminders, and whether the supplied content can be edited.
- Review the two content ambiguities before enabling reminders.
- No account required in the local-only proposal.

#### Today

- Date/day, activity title, workout duration estimate, training status, meal timeline progress, hydration/supplement check-ins if enabled.
- Primary action: Start or Resume workout/run; secondary actions: view session, mark recovery/mobility.
- Empty/rest-day state and missed-day behavior are open decisions.

#### Weekly plan

- Seven-day strip or list with focus and duration.
- Day detail includes warm-up, ordered exercises, cooldown, and plan notes.
- Clear indicators for completed, planned, skipped, or rescheduled sessions.

#### Guided workout

- Pre-session overview, warm-up checklist/timer, exercise cards, active set entry, rest timer, substitutions, pause/resume, finish summary.
- Per exercise: target sets/reps, RIR, rest, cues, and prior-session results. Show progression against the plan double-progression rule and flag a next-weight candidate when all sets meet the target range/RIR; never silently rewrite targets or weights.
- Set logging fields under consideration: load, reps, RIR, completion; duration/distance for timed or carry/run items.
- Large touch targets and one-handed operation; keep the active set and timer prominent.
- Confirm destructive actions such as discarding an in-progress session.

#### Food timeline

- Meal/time, plan text, done/skipped state, and optional notes.
- Hydration entry may be quick-add amounts or simple checkbox; exact behavior is open.
- Supplements can be grouped within meals or tracked separately. The confirmed creatine reminder is a single 5 g breakfast check-in.
- Do not calculate nutrient totals unless the user requests it and supplies/approves the data source.

#### Progress and history

- Session list and exercise-level history.
- Body weight trend, measurement timeline, and PR markers.
- Decide if progress photos are in scope; if yes, keep local-only by default and make deletion/export clear.
- Charts should have text summaries and work with accessibility settings.

#### Settings and data management

- Units, theme, reminder times/days, haptics, sound, voice/audio, animation reduction, plan editing, export/import, reset/delete data.
- Explain notification, audio, and photo permissions only when their feature is enabled.

## 5. Workout interaction details (proposal)

- Starting a workout creates a dated session snapshot so later plan edits do not rewrite history.
- The active exercise shows the target and cue, with a compact set table and a direct “Log set” action.
- On logging a set, optionally start the configured rest timer; timer can be skipped, paused, or adjusted.
- Timer completion may use vibration, a short sound, or both, each independently configurable and subject to device silent/DND behavior.
- Allow corrections to logged sets, exercise skipping, substitution, session pause/resume, and a completion summary.
- Preserve partially completed workouts across app restarts.
- Decide later whether users can reorder exercises, add free-form exercises, or edit targets.

## 6. Diet and daily routine interaction details (proposal)

- Present meal instructions as written, in chronological order, with completion toggles.
- Optional scheduled reminders should be opt-in and editable, with Android notification permission requested in context.
- Hydration tracking needs a chosen model (daily target with amount entry, preset quick-add buttons, or a simple reminder/checklist).
- The supplied food quantities and supplement list are displayed as plan content; no ingredient database or nutrition calculation is assumed.
- Allow marking a meal skipped without deleting it; decide whether notes/portion edits are needed.

## 7. Visual design and layout direction — user direction and recommendations

### Confirmed visual direction

- Pixel-inspired Material 3 Expressive with the user’s requested micro-interactions.
- Use **Google Sans Flex throughout the app**, with the roundness axis set to **ROND = 100** (maximum; the font axis range is 0–100). Bundle the variable font locally so rendering has no runtime font download, and include its license notice. Tune weight, width, and optical size per role while keeping roundness at 100. Google Sans Flex is a six-axis variable font; Compose exposes variation settings for supported font axes. Sources: [Google Sans Flex axis ranges](https://github.com/googlefonts/googlesans-flex), [Compose font variation API](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/font/FontVariation), [Google Sans Flex design notes](https://design.google/library/google-sans-flex-font).
- “Systemwide” means every text role inside this app. An ordinary app cannot replace Android’s system font in other apps or system settings.
- Use a fixed, app-owned color palette; do not derive the app’s colors from wallpaper. **BLOCK (workout)** uses a black-and-white/monochrome foundation with restrained accent UI. **FUEL and the other screens** use a fuller, colorful Material 3 Expressive palette, coordinated with BLOCK. Exact accent hues, neutral values, and light/dark palette are open.
- Name the exercise section **BLOCK** and diet section **FUEL**, as requested.
- **BLOCK presentation:** typography only for hero treatments; no exercise photography/video. Follow the information hierarchy and generous workout-card treatment in screenshots 1–4: workout identity/metadata, warm-up and ordered exercises grouped into clear cards, prescriptions and cues, then a prominent Start Workout action. This is a layout/style reference only; use the user's source plan for every exercise, target, title, cue, and instruction.
- **FUEL presentation:** distinct from BLOCK and creatively expressive within Material 3 Expressive. A meal timeline/check-in experience is the approved direction; do not invent per-meal calories/macros or depict completion as a nutritional measurement. The source diet plan supplies the content.
- **Dock presentation:** adapt the fifth screenshot's floating-pill and active-selection concept to Today · BLOCK · FUEL · Progress. Keep all destination names legible. Ignore the screenshot's colors and sample labels. A separate circular quick-action control is not selected.
- **App identity:** app name is **BLOCK**. Make the launcher icon an adaptive monochrome-first typographic/block mark that reads at small sizes; a simple “B” built from a compact grid or cutout block is the leading concept, with one restrained accent layer. Exact icon artwork is open.

### Typography implementation direction

- Use the same bundled Google Sans Flex variable font for headings, body copy, exercise/meal details, numerals, and controls. Do not use Roboto as an intentional font choice.
- Set `ROND` to 100 throughout. Choose readable role sizes/weights and optical sizing; avoid novelty letter spacing or excessive all-caps outside the BLOCK/FUEL section names.
- Keep text scalable with Android font-size accessibility settings and test wrapped long exercise/meal instructions. If the chosen font asset lacks a glyph, determine an explicit non-Roboto fallback rather than silently selecting Roboto.

### Icons and app icon

- In-app icon direction remains Material Symbols Rounded, adding only the specific vector assets actually used.
- Launcher icon proposal: Android adaptive icon with foreground/background and monochrome layers. Leading direction is a typographic/block “B” mark that reads at small sizes, with one restrained accent layer. Final vector artwork remains open.
- Keep navigation icon + text labels visible in the dock pill; do not require users to recognize icon-only destinations.

### Phone layout proposal

- Bottom dock pill: **Today · BLOCK · FUEL · Progress** (confirmed). The active destination gets a clear expressive selection state with a short spring-like indicator morph and icon-state transition; labels stay visible. Use a custom-shaped Material 3 navigation surface rather than treating an action toolbar as destination navigation. Shape/selection treatment is inspired by screenshot 5; its colors are excluded as a reference.
- **Today:** date and compact week strip; one large workout/run card with Start/Resume; next meal and chronological meal checklist; compact mobility/recovery item. Use the colorful expressive system rather than BLOCK's monochrome presentation.
- **BLOCK:** use a calm, high-contrast NTC-inspired workout information layout: monochrome foundation with restrained accent UI; typography-only header treatment; workout name/focus and plan-sourced metadata; then separate warm-up, exercise/circuit, and cooldown groupings with generous spacing and clear dividers/cards. Each item shows only prescription fields present in the plan (sets, reps/time/distance, RIR, rest, cues, alternatives). Make technique cues expandable when needed to keep cards readable. Pin a large **Start Workout** action at the bottom of workout detail. No NTC branding, screenshot wording, or exercise photography/video.
- **FUEL:** use a more expressive meal-routine timeline with distinct meal moments, time labels, warm accent, varied but controlled shapes, and satisfying check-in motion. Keep meal text readable and completion state clear. Keep the 5 g creatine check-in at breakfast. Do not imply that completion represents calories, macros, or actual nutrition quality.
- **Progress:** session history and exercise progression first, then body-weight/measurement trends and records. Use colorful expressive charts and status cues with text labels and accessible contrast.
- **Edit Plan:** separate focused editor reached from BLOCK or FUEL. Default editor section to the section from which it was opened; save/cancel behavior and reset-to-source remain open.
- **Active workout:** hide the dock, emphasize current exercise and cue, show target and previous-session result, use editable set rows, and pin a wide Log Set action above the system navigation area.
- **Settings:** top app bar action. Respect Android 16 edge-to-edge insets, portrait use, landscape, and large text without fixed controls covering content.

### Color direction to refine

User direction: **BLOCK** is black-and-white with restrained accent UI; **FUEL**, Today, and Progress use a fuller colorful Material 3 Expressive treatment. Recommendation: keep the BLOCK workout view primarily monochrome, then carry one of its accents into FUEL's palette alongside warmer hues so the app still feels like one family. Avoid deriving colors from wallpaper or copying the screenshot colors. Exact color tokens and light/dark variants remain open.

## 8. Haptics, audio, navigation, and motion

### Haptics

- Candidate events: set saved, rest timer completed, session completed, validation/error.
- Keep feedback brief and distinct; provide a global toggle and respect system settings. Avoid vibration on every navigation tap by default.
- Decide whether intensity/event-level controls are needed.

### Audio and spoken guidance

- Candidate: optional spoken exercise/meal instructions via Android text-to-speech, and optional brief timer-complete sound.
- Text-to-speech can use installed system voices and may need a first-use language/availability check; no bundled audio files are assumed.
- Audio must be opt-in, interruptible, and independently disableable. Decide whether workouts should continue spoken prompts with screen off and whether audio focus should pause other media.

### Navigation and gestures

- Prefer visible buttons and standard Android back behavior; don't make swipe gestures the only way to act.
- Possible gestures: swipe between plan days or dismiss a timer, only if discoverable and accessible.
- Confirm before discarding session data; provide undo for accidental completion/skip where feasible.

### Animations

- Short transitions for navigation and completion feedback; subtle timer/progress changes.
- No animation should delay logging or obscure controls. Respect system “remove animations” / reduced motion.
- Exact motion language and durations remain open.

## 9. Data, privacy, and offline behavior

### Local-only behavior (confirmed)

- Core plan, settings, and logs available offline.
- Keep workout and meal data on-device; no account or network dependency in the initial proposal.
- No cloud sync or cloud backup. Android Auto Backup is enabled by default for eligible apps and includes app data unless configured otherwise. Set the app to opt out of cloud backup and use explicit data-extraction rules to exclude local plan/log data from cloud backup and device transfer; verify behavior on the target Android 16 phone because device-to-device handling can vary by manufacturer. No manual file export is planned unless requested. Source: [Android Auto Backup guidance](https://developer.android.com/identity/data/autobackup).
- Ask before including photos, location, or health metrics beyond manually entered values.

### Data model concepts

- Plan template → week/day → activity → warm-up/exercise/meal item.
- Exercise prescription includes target sets, rep/time/distance range, RIR, rest, cues, and substitutions.
- Session instance stores date, completion state, plan snapshot, set logs, substitutions, and notes.
- Daily check-ins store meal/water/supplement status and optional values.
- Progress entries store dates and user-entered weight/measurements; photos only if explicitly in scope.
- Plan versioning/migration behavior must be chosen before implementation.

## 10. Technical direction — user-approved direction; versions to be pinned at implementation

### UI stack options

| Option | Fit for this app | Tradeoff |
|---|---|---|
| **Kotlin + Jetpack Compose** | Best fit. It is Google's native declarative Android UI toolkit; Compose Material 3 is the direct route to Google's Android design components, theming, and micro-interactions. It aligns especially well with an Android 16 target and Android-only scope. | Kotlin/Compose conventions are needed; some of the newest expressive APIs are currently pre-release. |
| Kotlin + XML Views | Still a valid native option and can use Android Material components. | Requires the traditional View/layout approach; expressive components and state-driven micro-interactions are less direct for a new app. |
| Flutter or React Native | Consider if a shared Android/iOS app becomes a real requirement. | Adds a separate UI toolkit and ecosystem to an Android-only project, with less direct use of native Compose Material 3 APIs. |

**Recommendation:** Kotlin + Jetpack Compose. Google's Compose guidance describes Compose as supporting Material 3 Expressive and specifically says it complements Android 16. Material 3 research guidance emphasizes expressive color, shape, size, motion, and containment while keeping familiar interaction patterns and accessibility. This matches the requested Pixel-like feel without forcing visual flourishes into every screen. Sources: [Compose Material 3 guidance](https://developer.android.com/develop/ui/compose/designsystems/material3), [Google's M3 Expressive research summary](https://design.google/library/expressive-material-design-google-research).

### Stable Material 3 vs the newest Expressive APIs

As of 2026-09-27, Android's release page lists **Material 3 1.4.0 as stable** and **1.5.0-alpha29 as alpha**. Some APIs explicitly named for the newest expressive experience—such as `MaterialExpressiveTheme`, `expressiveLightColorScheme`, and the built-in expressive `MotionScheme`—are listed as added in 1.5.0-alpha29. The 1.5 line is still pre-release and may change. Sources: [Compose Material 3 release status](https://developer.android.com/jetpack/androidx/releases/compose-material3), [MaterialExpressiveTheme API status](https://developer.android.com/reference/kotlin/androidx/compose/material3/MaterialExpressiveTheme.composable), [MotionScheme API status](https://developer.android.com/reference/kotlin/androidx/compose/material3/MotionScheme).

**Selected direction (delegated by the user):** Use the current Material 3 Expressive APIs that provide the strongest Compose micro-interactions. As of 2026-09-27, pin Material 3 1.5.0-alpha29 for the expressive theme/motion APIs, with Compose as the UI stack. Keep use of alpha-specific APIs limited to interactions that add clear value; the tradeoff is pre-release API churn. Recheck the official release page and verify API/toolchain compatibility when implementation begins; if a newer stable release offers the same expressive motion, prefer that stable release.

### Lean Android-native candidate

- Kotlin, Android Gradle Plugin, Gradle Kotlin DSL, Jetpack Compose, and Compose Material 3. Keep the app single-module unless real scale justifies more.
- Use official AndroidX/platform APIs where they cover the need. Avoid network, analytics, dependency-injection, chart, icon-bundle, or animation libraries unless a chosen feature requires them.
- The app needs persistent editable plan data and detailed workout history. A structured on-device database is therefore likely; choose the smallest maintainable storage approach after confirming local-only versus cloud behavior.
- Seed the app with a structured representation of the two supplied text plans. Edits in the Edit section should persist, while completed workout sessions retain a snapshot so later plan edits do not rewrite history.
- For icons, prefer only the required Material Symbols as small vector assets rather than a large bundled icon dependency. The current Compose Material 3 release notes say the old Compose Material Icons library is no longer recommended and can increase build time.
- Use native text-to-speech and vibration APIs if those features are selected; avoid bundling audio files by default.
- Pin a compatible stable toolchain/dependency set in the Gradle wrapper and version catalog. Exact versions are chosen when implementation begins, after checking current Android compatibility.

### Android 16 and arm64-v8a output

- Build and target Android 16 (API 36) for the stated phone. The minimum supported API level remains an open decision; supporting only Android 16 would be a separate choice.
- The app is expected to install and run on an arm64-v8a phone. Pure Kotlin/Compose code is packaged as Android bytecode and does not require an ABI-specific native build. Keep native libraries out unless necessary; if a selected dependency includes native binaries, restrict/package the APK for `arm64-v8a` and verify that ABI in CI.
- The CI artifact should be a debug APK installable on the target phone. Keep release signing credentials out of the repository.

### GitHub Actions build requirement

- Add a workflow for pushes and pull requests to compile the app from a clean checkout with the Gradle wrapper and a JDK compatible with the selected Android Gradle Plugin.
- Cache Gradle dependencies, run the debug APK assemble task, and upload the APK as a workflow artifact.
- No keystore or secrets are required for a debug APK. Release signing and Play Store publishing are outside the current scope.
- Select exact CI action versions, JDK, Android SDK packages, task path, and artifact retention after the Android project structure and toolchain are selected.

### Technical choices still open

- Package/application ID; minimum Android API; persistence technology; plan content format and reset/version semantics; reminder scheduling; exact compile/target SDK values; and release strategy.
## 11. Accessibility, reliability, and acceptance criteria proposal

- Core screens usable with TalkBack, large system text, sufficient contrast, and reduced motion.
- Core workout and plan screens work offline.
- A user can begin, pause, resume, log, correct, and finish a workout without losing data on process death/restart.
- The plan content is represented faithfully after ambiguities are resolved; substitutions remain visible and tied to the replaced exercise.
- Meal and reminder completion does not imply medical validation of the source plan.
- User can inspect and delete locally stored records. No cloud sync, cloud backup, or silent Android Auto Backup.
- GitHub Actions produces a successful debug APK artifact from a clean checkout using documented toolchain settings.
- Performance, accessibility verification depth, and test/CI checks are to be decided before implementation.

## 12. Open decisions / conversation queue

### Recorded from the user's answers

- **Plan content and editability:** Use the attached gym/diet plans as the initial app content; provide an in-app Edit section.
- **Progress:** Track workout progression with set-by-set history and user-controlled progression cues.
- **Creatine:** One 5 g check-in at breakfast.
- **Device target:** Android 16 / API 36, arm64-v8a phone.
- **UI/motion direction:** User delegates the choice to the option with the best available micro-interactions. Use Kotlin + Jetpack Compose and the current Material 3 Expressive APIs that provide the best fit; pin versions and recheck compatibility when implementation begins. As checked 2026-09-27, that points to Material 3 1.5.0-alpha29 for named expressive theme/motion APIs.
- **Data:** Plan edits and logs stay on the phone. No cloud sync or cloud backup.
- **Visual references:** Screenshots 1–4 guide BLOCK's workout information layout only. Screenshot 5 guides floating dock shape/active selection only; its colors are not a reference. Screenshot text, sample exercises, branding, and photography are not app content.
- **Navigation labels:** Today · BLOCK · FUEL · Progress are confirmed.
- **Section visual distinction:** BLOCK retains the screenshot-inspired workout style; FUEL gets its own more expressive Material 3 treatment.

### Design decisions and remaining visual choices

1. **Fonts:** confirmed — Google Sans Flex throughout the app, `ROND=100`. This means app-wide type, not changing Android system settings outside the app.
2. **Navigation/layout:** confirmed — floating pill-shaped dock with Today · BLOCK · FUEL · Progress; active selection adapts screenshot 5; Settings in the top app bar; Edit Plan accessible from BLOCK and FUEL.
3. **BLOCK layout:** confirmed direction — use the first four screenshots for roomy workout detail and grouped exercise-card hierarchy; typography only, no exercise media.
4. **FUEL layout:** confirmed direction — full colorful Material 3 Expressive treatment; meal-moment timeline/check-ins approved. Detailed card and color choices remain open.
5. **App name:** confirmed — **BLOCK**.
6. **App icon:** open — recommended adaptive typographic/block “B” mark, designed monochrome-first to read at small sizes with one restrained accent layer.
7. **Colors:** confirmed — fixed app palette, not wallpaper-dependent; BLOCK uses black/white with restrained accent UI, while FUEL and other screens use fuller colorful Material 3 Expressive styling. Exact hues, light/dark behavior, and contrast tuning remain open.
8. **Imagery:** confirmed — typography only for BLOCK header/hero treatments; no exercise photos or videos.

### Next after these design choices

- Final launcher-icon artwork and visual identity details.
- Calendar-week mapping vs next-session sequence; start date and ramp-up behavior.
- Exact Edit-section fields, draft/save behavior, and reset/version semantics.
- Meals: checklist vs amounts; water tracking and reminders.
- Progress measures, photos, and chart needs.
- Audio/TTS and notification behavior.
- Minimum Android API, privacy, package ID, persistence format, GitHub Actions triggers/artifact policy, and release scope.

## 13. Decision log

| ID | Topic | Status | Decision |
|---|---|---|---|
| D-01 | Native UI stack | Confirmed direction | Kotlin + Jetpack Compose |
| D-02 | Material 3 micro-interaction APIs | User delegated | Use current best expressive APIs; pin version; currently 1.5.0-alpha29 |
| D-03 | Initial plan content and editing | Confirmed | Use attached plans as seed; provide an in-app Edit section |
| D-04 | Workout progression tracking | Confirmed | Log sets and retain history to track progression |
| D-05 | Creatine check-in | Confirmed | 5 g at breakfast, once daily |
| D-06 | Device target | Confirmed | Android 16 / API 36, arm64-v8a phone |
| D-07 | Data location | Confirmed | Local-only; no cloud sync or cloud backup |
| D-08 | Typography | Confirmed | Google Sans Flex throughout app; ROND=100; no Roboto |
| D-09 | Main sections and dock | Confirmed | BLOCK (exercise), FUEL (diet), floating pill dock destinations Today · BLOCK · FUEL · Progress; active-selection treatment inspired by screenshot 5 |
| D-10 | Palette behavior | Confirmed | Fixed palette, not wallpaper-derived; BLOCK uses black/white with restrained accent UI; FUEL and other screens use fuller colorful Expressive styling |
| D-11 | Launcher icon and app name | Partially confirmed | App name BLOCK; adaptive typographic/block “B” icon proposed, final mark open |
| D-12 | BLOCK layout reference | Confirmed direction | Screenshots 1–4 guide workout-card hierarchy and detail presentation only; sample content, photography, and branding are excluded |
| D-13 | FUEL visual treatment | Confirmed direction | Fuller colorful Material 3 Expressive treatment; meal timeline/check-ins approved, exact palette and detailed layout open |
| D-14 | Dock reference | Confirmed direction | Screenshot 5 guides floating-pill shape and selected destination treatment; screenshot colors and labels are excluded |
| D-15 | BLOCK hero imagery | Confirmed | Typography only; no exercise photos or video |
| D-16 | App name | Confirmed | BLOCK |

---

**Implementation record:** The app source, launcher resources, bundled font, license, Gradle wrapper, and GitHub Actions workflow are present in this repository. The workflow is configured to build a debug APK on GitHub and upload it as a workflow artifact. No local Gradle build has been run; compilation remains to be confirmed by GitHub Actions.

## 14. Implemented baseline and applied defaults

- **User-confirmed:** BLOCK app name; Google Sans Flex throughout with `ROND=100`; typography-only BLOCK presentation; monochrome BLOCK with restrained accents; fixed colors; colorful Material 3 Expressive FUEL and supporting screens; Today · BLOCK · FUEL · Progress floating pill dock; Android 16 / API 36 arm64-v8a; local editable plan data; progression logging; 5 g creatine at breakfast; no cloud sync or backup; best-fit micro-interactions; fill reasonable quality-of-life gaps.
- **Applied defaults:** Calendar weekday schedule with missed sessions left in place; weeks 1–2 at 2 sets and RIR 3–4, weeks 3–4 at 3 sets and RIR 2–3, then source prescriptions; simple meal and hydration check-ins; locally editable schedule content; optional haptics, timer tone, and spoken cues; rest timer and session recovery; editable bodyweight/measurements and workout history.
- **Build target:** JDK 17, AGP 9.1.1, Gradle 9.3.1, compile SDK 37, target SDK 36, minimum SDK 26. APK ABI is restricted to arm64-v8a. Dependency and plugin versions are pinned in Gradle configuration.
- **CI:** `.github/workflows/android.yml` builds `assembleDebug` on pushes and pull requests and on manual dispatch, then publishes `app-debug.apk` as a 14-day artifact. Push the repository contents to GitHub to start the workflow. GitHub Actions steps use maintained releases; Gradle wrapper validation and basic dependency caching are configured.
- **Local-only storage:** User edits, check-ins, and logs are persisted on device. Android backup and device-transfer backup are disabled and there is no network permission or sync implementation.
- **Unverified:** The implementation has not been compiled locally or by GitHub Actions. Review the first Actions run before treating the APK as build-verified; this was intentionally prepared as source for GitHub rather than built on this PC.





