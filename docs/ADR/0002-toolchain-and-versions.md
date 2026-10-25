# ADR 0002: Toolchain and dependency versions

- **Status:** Accepted
- **Date:** 2026-10-25

## Context

Versions must be stable and mutually compatible, and must be verified rather than guessed. On
2026-10-25 they were checked against Google Maven, Maven Central, services.gradle.org and the
AGP release notes.

## Decision

| Component | Version | Reason |
|---|---|---|
| AGP | 9.4.1 | Latest stable. Requires Gradle ≥ 9.6.0, JDK 17 and Build Tools 36.0.0. Supports API levels up to 37. |
| Gradle | 9.8.2 | Latest stable and above AGP's minimum. |
| Kotlin | 2.4.21 | Latest stable. AGP 9 ships *built-in Kotlin* that depends on KGP 2.2.10. Declaring `org.jetbrains.kotlin.android` with `apply false` in the root build raises it to 2.4.21. The Compose compiler plugin version must equal the Kotlin version. |
| JDK | 17 | AGP's minimum, and the JDK already installed (`JAVA_HOME`). |
| compileSdk | 37 | `core-ktx` 1.19.x and `navigation3` 1.2.0 declare `minCompileSdk=37` in their AAR metadata. |
| targetSdk | 36 | Behaviour changes for API 37 haven't been reviewed yet, and the local emulator runs API 36. Lint flags this (`OldTargetApi`), and we accept the warning. Raise it in a dedicated change after reading the API 37 behaviour-change notes. |
| minSdk | 26 | Supports adaptive icons, `java.time` without desugaring and modern Keystore APIs, and still reaches nearly all active devices. |
| Compose BOM | 2026.09.00 | Latest stable BOM (Material 3 1.4.0). |
| kotlinx-serialization | 1.11.0 | 1.12.0 was released on 2026-10-19 and is too new to adopt immediately. Lint flags this (`NewerVersionAvailable`), and we accept the warning. |

## Consequences

- **AGP 9 built-in Kotlin:** modules apply only `com.android.application`, *not*
  `org.jetbrains.kotlin.android`. Many older tutorials show the latter, which errors on AGP 9.
- **Known build warning:** "Deprecated Gradle features … incompatible with Gradle 10" comes from
  a plugin calling `Configuration.setVisible`, not from our scripts (verified with
  `--warning-mode all`).
- SDK Platform 37 had to be installed (`sdkmanager "platforms;android-37.0"`).
