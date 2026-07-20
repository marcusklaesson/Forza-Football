# Forza Football – World Cup Teams

Android app that fetches the World Cup teams and displays them in a sortable list, with a team details sheet and a two-team comparison view.

## Features

- Fetches teams from `world-cup-teams.json` and shows them in a `LazyColumn` with team badges (Coil).
- Sorting via a dropdown: name, world rank, WC titles, WC participations, population, registered players.
- Tap a team → details bottom sheet with all stats.
- Long-press two teams → comparison bottom sheet with the better value per stat highlighted (lower is better for world rank).
- Loading, error/retry and empty states, plus an in-list hint for the tap/long-press gestures.

## Tech stack

- Kotlin, Jetpack Compose (Material 3), single-Activity
- MVVM: `ViewModel` + `StateFlow` → `collectAsStateWithLifecycle`
- Retrofit + kotlinx.serialization for networking, Coil 3 for badge images
- Repository layer wrapping the API behind `Result`

## Architecture & decisions

- **Single UI state object** (`WorldCupUiState`) exposed as one `StateFlow`. Keeps the screen a pure function of state and makes loading/error/content handling trivial in one `when`.
- **Feature-based packaging** (`worldcup/model|remote|repository|presentation` + shared `core` and `ui/theme/components`). Scales better than layer-only packaging if more features are added.
- **Sorting modelled as an enum** (`SortOption`) carrying its own `Comparator` and display formatting. Adding a new sort option is a one-line change, and every sort uses name as a stable tie-breaker so ordering is deterministic.
- **Manual DI via default constructor parameters** (`NetworkModule` singleton, repository/API defaults). Hilt/Koin felt like overkill for one screen, but the constructor injection points mean the ViewModel and repository are still testable with fakes.
- **Errors surfaced as `Result`** from the repository with `runCatching`, mapped in the ViewModel to user-friendly messages (offline vs. generic) and shown on an error screen with retry. Raw `Throwable.message` is never shown to the user.
- **All sheet/selection state lives in the ViewModel** (`selectedTeam`, `comparisonTeams`) so the UI is a pure function of one state object and survives configuration changes.
- **Comparison selection logic in the ViewModel**: long-press toggles selection; picking a third team replaces the oldest so the user never hits a dead end.
- **Badges built by convention** from `images.multiball.forzafootball.net/badges/team/thumbnail/{teamId}.png` since the feed only provides team ids.
- **Lenient JSON parsing** (`ignoreUnknownKeys`, `coerceInputValues`) so feed additions don't break the app.

## Trade-offs

- **No local caching / offline support** – data is refetched on process death; acceptable for a single static feed.
- **Sorting is done eagerly in the ViewModel** (stored sorted in state) rather than derived; simpler, but sort logic runs on the main thread – fine for 32 teams.
- **No DI framework, no navigation library** – both would add setup cost with little benefit at this size.
- **Strings are hardcoded** in composables instead of `strings.xml`; faster to iterate, but not localization-ready.
- **Minimal test coverage** – the architecture is test-friendly (injected dispatcher, fakeable repository/API) but tests weren't prioritized within the timebox.

## What I'd tackle next

1. **Unit tests**: `WorldCupViewModel` (load success/failure, sort, comparison selection rules) and `SortOption` comparators, using a fake repository and a test dispatcher.
2. **UI tests** for the list, sorting and bottom sheets with Compose testing APIs.
3. **Offline-first caching** (Room or a simple disk cache) with pull-to-refresh.
4. **Richer error** (HTTP status–specific messages) and localized error strings.
5. **String resources + localization**, content descriptions and a11y pass.
6. **Search/filter** in the list.
7. **CI** (GitHub Actions running lint + unit tests) and a debug/release build split with proper R8 config.
