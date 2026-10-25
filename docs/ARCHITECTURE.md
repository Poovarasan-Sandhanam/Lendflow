# Architecture

This document records what is **implemented** and, separately, the **target** the later phases move toward.

## Implemented (Phase 0)

```text
MainActivity (single Activity, enableEdgeToEdge)
 └── LendFlowTheme (Material 3, fixed brand colours, light + dark)
      └── LendFlowNavDisplay (Navigation 3)
           ├── DashboardRoute  → DashboardScreen(onOpenCalculator)
           └── CalculatorRoute → CalculatorScreen(onBack)
```

- **Single Activity.** Every screen is a composable. The Activity only sets the theme and hosts navigation.
- **Navigation 3.** The back stack is an observable `List<NavKey>` that we own
  (`rememberNavBackStack`). Routes are `@Serializable`, so the stack survives configuration
  changes *and* process death. See [ADR 0003](ADR/0003-navigation-3.md).
- **Screens take lambdas, not navigators.** `DashboardScreen(onOpenCalculator = …)` knows nothing about
  routes or back stacks. That keeps screens previewable and testable in isolation, and keeps
  navigation independent of data access.
- **Theme.** Dynamic colour is deliberately off, so the brand looks the same on every device.
  Headline and title styles use tabular figures (`tnum`) so that amounts line up.
- **Backups disabled.** `allowBackup=false`, plus exclusion rules for Auto Backup (API ≤ 30) and
  for cloud backup and device-to-device transfer (API 31+). Session and cached loan data must
  not leave the device.

## Target architecture (later phases)

```text
UI event → ViewModel → use case (when it adds logic) → Repository → Room / Retrofit
                ↑                                                     │
                └──────────── StateFlow<UiState> ◄── Flow ◄───────────┘
```

| Layer | Package | Depends on | Must not depend on |
|---|---|---|---|
| UI | `feature/*` | ViewModel, `core/designsystem` | Retrofit, Room, Activities |
| ViewModel | `feature/*` | `domain` | Android UI types, DTOs, entities |
| Domain | `domain/{model,repository,usecase}` | Kotlin stdlib + coroutines only | Android, Retrofit, Room |
| Data | `core/{network,database,datastore}`, repository impls | domain contracts | UI |
| Sync | `sync/` | repositories | UI |

Rules that apply from Phase 1 onward:

- Room is the single source of truth for loan data. The network only *refreshes* Room.
- Domain models are separate from DTOs and Room entities, and explicit mapper functions convert between them.
- ViewModels expose an immutable `StateFlow<UiState>` and receive events as function calls.
- Use-case classes exist only when they hold real business logic, never as one-line pass-throughs.
- Gradle modules are split out only when there's a concrete benefit (see [ADR 0001](ADR/0001-single-module-feature-packages.md)).
