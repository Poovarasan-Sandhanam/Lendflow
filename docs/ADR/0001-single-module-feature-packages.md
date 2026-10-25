# ADR 0001: One Gradle module, feature-oriented packages

- **Status:** Accepted
- **Date:** 2026-10-25

## Context

The target structure has `core/*`, `feature/*`, `domain/*` and `sync/` areas. Each could be its own
Gradle module, as in large production apps (for example Now in Android).

## Decision

Start with one `:app` module and express the boundaries as packages:
`com.lendflow.core.*`, `com.lendflow.feature.*`, `com.lendflow.domain.*`, `com.lendflow.sync`.

## Consequences

- **Pro:** one build file, a fast setup and no convention plugins to maintain. All attention goes
  to Android concepts rather than build plumbing.
- **Con:** the compiler doesn't enforce the boundaries, so a feature *could* import Room directly.
  We rely on code review and the rules in `ARCHITECTURE.md`.
- **Revisit when** one of these appears: build times that hurt, a need to prove that `domain` has no
  Android dependency (a pure Kotlin/JVM module would enforce it), or a need for reuse across apps.
  The likely first split is `:domain` as a pure JVM module.
