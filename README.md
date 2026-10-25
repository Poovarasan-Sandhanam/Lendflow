# LendFlow

A simulated personal-lending app for Android, built with Kotlin and Jetpack Compose as a structured
learning project for moving from React Native to native Android.

> **Simulation only.** No real money, credit checks, identity verification or payment processing.
> Use synthetic data only.

## Status

| Phase | Scope | Status |
|---|---|---|
| 0 | Toolchain, project skeleton, theme, navigation, docs | ✅ Done |
| 1 | Loan calculator (pure Kotlin domain + ViewModel + tests) | Planned |
| 2 | Retrofit + Room, offline-first dashboard | Planned |
| 3 | Hilt dependency injection | Planned |
| 4 | WorkManager background sync | Planned |
| 5 | Loan application state machine, KYC (CameraX) | Planned |
| 6 | Secure session, biometric unlock, repayments | Planned |
| 7 | Performance, CI/CD, release process | Planned |
| 8 | React Native → native migration case study | Planned |

## Toolchain

| Tool | Version |
|---|---|
| JDK (Gradle daemon and toolchain) | 17 (Zulu 17.0.18) |
| Gradle (wrapper) | 9.8.2 |
| Android Gradle Plugin | 9.4.1 |
| Kotlin | 2.4.21 (AGP built-in Kotlin) |
| compileSdk / targetSdk / minSdk | 37 / 36 / 26 |
| Android Studio | 2026.2 |

All library versions live in [`gradle/libs.versions.toml`](gradle/libs.versions.toml). See
[ADR 0002](docs/ADR/0002-toolchain-and-versions.md) for why each was chosen.

## Getting started

Requirements: JDK 17 on `JAVA_HOME`, plus Android SDK Platform 37 and Build Tools 36.0.0 installed.
Android Studio creates `local.properties` (`sdk.dir=...`) automatically. On the command line, create it yourself.

```bash
./gradlew assembleDebug              # build the debug APK
./gradlew installDebug               # install on a running emulator/device
./gradlew lintDebug                  # Android lint
./gradlew connectedDebugAndroidTest  # instrumented UI tests (needs emulator/device)
```

## Project layout

A single `:app` Gradle module, organised by package (see [ADR 0001](docs/ADR/0001-single-module-feature-packages.md)):

```text
app/src/main/java/com/lendflow/
├── LendFlowApplication.kt
├── MainActivity.kt
├── navigation/            # NavKeys + NavDisplay (Navigation 3)
├── core/designsystem/     # theme, colors, typography, spacing
└── feature/
    ├── dashboard/         # placeholder until Phase 2
    └── calculator/        # placeholder until Phase 1
```

`domain/`, `core/network`, `core/database`, `sync/` and the other packages are added when the phase
that needs them begins, not before.

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [Learning log](docs/LEARNING_LOG.md)
- [Architecture decision records](docs/ADR/)
