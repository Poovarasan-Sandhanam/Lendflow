# Learning log

## Phase 0: Environment and skeleton (2026-10-25)

### Concepts

**Gradle build anatomy** (≈ `package.json` + Metro + the native build, combined)
- `settings.gradle.kts` declares repositories and modules. `build.gradle.kts` (root) declares plugin
  versions. `app/build.gradle.kts` configures the Android app.
- `gradle/libs.versions.toml` is the *version catalog*, a single place for versions that's referenced
  as `libs.androidx.core.ktx`. It's the closest equivalent to a lockfile-less `package.json`. Gradle
  doesn't pin transitive versions by default.
- The **Gradle wrapper** (`./gradlew`) pins the Gradle version per project, much like `.nvmrc`.
- A **BOM** (Compose BOM) is a single version that aligns many artifacts, so the Compose libraries need no
  individual versions.

**Android SDK levels**
- `compileSdk`: the API you compile *against*. Libraries can demand a minimum through AAR metadata.
- `targetSdk`: the platform behaviours the app opts into. Raising it is a behaviour change and needs testing.
- `minSdk`: the oldest OS that can install the app.

**App entry points** (≈ `index.js` → `AppRegistry` → root component)
- `Application`: one per process, created first. Hilt attaches here later.
- `MainActivity`: the single Activity that hosts Compose through `setContent { }`.
- `enableEdgeToEdge()`: draws behind the system bars. `Scaffold` supplies the insets as padding.

**Window theme vs Compose theme.** The XML theme in `res/values*/themes.xml` styles the *window*
before Compose draws (status bar icon colour, background during launch). The Compose `MaterialTheme`
styles everything after that. **Bug found and fixed:** with only a light XML theme, dark mode showed
dark status bar icons on a dark background. A `values-night/themes.xml` fixed it. Android picks
resource folders by configuration qualifiers (`-night`, `-v31`, `-sw600dp`), which has no RN equivalent.

**Navigation 3.** See ADR 0003. The back stack is a `SnapshotStateList<NavKey>`. Changing it triggers
recomposition, and `NavDisplay` shows the last entry.

**Process death vs configuration change.** Rotation recreates the Activity. Android can also
*kill the process* in the background and later restore it from a saved `Bundle`.
`rememberNavBackStack` persists to that Bundle, which is why routes are `@Serializable`. The
instrumented test `backStackSurvivesActivityRecreation` covers recreation. True process death was
verified by hand: open the Calculator, press Home, wait a few seconds, run `adb shell am kill com.lendflow`
(it does nothing until the app is fully backgrounded), and confirm `pidof` is empty. Relaunching
was a `COLD` start with a new PID, and the app restored to the Calculator screen.

Note: `connectedDebugAndroidTest` uninstalls the app when it finishes. Run `installDebug` again
before any manual checks.

### Commands run

```bash
./gradlew assembleDebug                 # BUILD SUCCESSFUL
./gradlew lintDebug                     # 2 accepted warnings (OldTargetApi, NewerVersionAvailable)
./gradlew connectedDebugAndroidTest     # 3 tests, 0 failures (Pixel_9 AVD, API 36)
```

### Production mistakes to avoid
- Applying `org.jetbrains.kotlin.android` on AGP 9 (built-in Kotlin makes it an error).
- Leaving `allowBackup` at its default `true` in a finance app.
- Turning on dynamic colour in a brand-sensitive app.
- Forgetting a night window theme, which causes a white flash and unreadable status bar in dark mode.
