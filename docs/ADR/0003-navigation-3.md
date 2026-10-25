# ADR 0003: Navigation 3 instead of Navigation Compose 2.x

- **Status:** Accepted
- **Date:** 2026-10-25

## Context

Jetpack offers two Compose navigation libraries:

- **Navigation Compose 2.x** (`androidx.navigation:navigation-compose`, 2.10.2 stable). Its
  `NavController` owns the back stack, and routes are declared in a `NavHost` graph.
- **Navigation 3** (`androidx.navigation3`, 1.2.0 stable). Built for Compose: the back stack
  is a list that *you* own, and `NavDisplay` renders whatever is on top.

## Decision

Use Navigation 3.

## Rationale

- Back-stack state is ordinary Compose state, so adding, removing or replacing entries is plain list code
  that can be tested and reasoned about directly. This fits the project's emphasis on unidirectional data flow.
- It's Google's direction for new Compose apps, and it has adaptive multi-pane support (Scenes) that
  later phases could use on tablets.
- `@Serializable` `NavKey`s give type-safe arguments and survive process death without extra work.

## Trade-offs

- Fewer tutorials and Stack Overflow answers than Navigation 2.x.
- Deep links and ViewModel scoping per entry need explicit wiring (`lifecycle-viewmodel-navigation3`,
  added in Phase 1 when the first ViewModel appears).
- **React Native comparison:** React Navigation hides the stack inside its navigator. Navigation 3 makes
  it a value in your hands, closer to a Redux-managed route stack.
