# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Compile (use this to verify changes — always run without piping)
./gradlew compileKotlinJvm

# Run with hot reload (development)
./gradlew runHot

# Run tests
./gradlew test

# Lint check / format
./gradlew ktlintCheck
./gradlew ktlintFormat

# Package installers
./gradlew packageDmg   # macOS
./gradlew packageMsi   # Windows
```

## Module Structure

```
:core:mvi    → MVI base classes (MVIBase, MVIState, MVIAction, MVISideEffect, MVIContent composables)
:core:utils  → Coroutine utilities
:domain      → Models, repository interfaces, use cases
:data        → Repository implementations, file I/O (TOML-based persistence)
:view        → Reusable stateless Compose UI components and layout primitives
main (root)  → Screens, StateHolders, Koin DI modules, app entry point
```

Dependency direction: `main` → `view`, `data`, `domain`, `core:*`. Never import `main` from submodules.

## Architecture: MVI

Every screen follows the same pattern:

- **`*State : MVIState`** — immutable data class holding all UI state
- **`*Action : MVIAction`** — sealed class of user intents
- **`*SideEffect : MVISideEffect`** — sealed class for one-time events (navigation, toasts)
- **`*StateHolder : MVIBase<State, Action, SideEffect>`** — handles actions, calls use cases, emits state
- **`*Screen.kt`** — stateless Composable receiving state + onAction lambda

`MVIBase.update { }` mutates state. `MVIBase.sideEffect(effect)` emits one-time events.

In Compose, wire them with `MVIChildContent(mvi = stateHolder) { state, onAction -> Screen(...) }`.

## Layout Components (`:view`)

`ScreenLayout` — top-level shell: top bar, navigation rail, content area, right panel, dialog overlay.

`ThreePaneLayout(splitterState, left, center?, right?)` — resizable left pane + fixed-width right pane (default 300dp via `rightWidth`). Each slot is individually wrapped in a floating `Surface(RoundedCornerShape(12.dp))`. Gap between panes is created via content padding (not a visible divider). Used by Text, Screenshot, ScrcpyNewDisplay screens.

`HorizontalPaneLayout(splitterState, first, second)` — two-pane resizable split. Both panes are individually floating Surfaces.

Island Theme rule: **do not set `background(colorScheme.background)` inside these layout slots** — it overrides the Surface and breaks the floating appearance.

## Dependency Injection (Koin)

Three modules in `main/di/`:
- `domainModule` — all use cases as `factory {}`
- `repositoryModule` — repositories as `single {}` or `factory {}`
- `stateHolderModule` — state holders as `factory {}`

Initialized in `Main.kt` via `startKoin { modules(...) }`.

## Key Conventions

- Source sets use non-standard paths: `main/kotlin/` and `test/kotlin/` (configured in each module's `build.gradle.kts`)
- `Language` object in `:domain` holds all UI strings (no Android string resources)
- `UserColor` in `:view` provides theme-aware colors (`getSplitterColor()`, `getFloatingBackgroundColor()`)
- Color scheme: `background` = outer shell color, `surface` = floating panel color — keep these roles consistent
- Data is persisted as TOML files in the user config directory via `*FileCreator` classes in `:data`
