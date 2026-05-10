# Scientific Name Suggestions

> **Status:** approved
> **Created:** 2026-05-10
> **Last updated:** 2026-05-10

## Purpose & Value

Plant owners may know part of a plant's scientific name but not the exact spelling, or they may want to link their plant to an existing known plant entry without losing the plant name they already typed. This feature makes the scientific name field more helpful by showing live suggestions from the app's known plant list, preserving free-text entry, and carrying the user's current text into the full known plant search screen.

- **Problem:** The scientific name field currently behaves like plain text unless the user manually opens known plant search, and search does not start from what they already typed.
- **Value:** Users can discover and select known scientific names faster while still entering custom names for plants that are not in the known plant list.
- **Success metric:** Users can type a partial scientific name, see up to four relevant suggestions, select one without changing the plant's common name, and open known plant search with the typed text already filled and filtered.

## Goals & Non-Goals

### Goals

- Show live scientific name suggestions while the user types in the `Scientific name` field on both add and edit plant forms.
- Limit inline suggestions to the top four matching known plants.
- Match inline suggestions against scientific names only, not common names.
- Use the existing hardcoded known plant source for version 1, currently represented by `SampleKnownPlants`.
- Allow the full known plant search screen to open with the current scientific name text prefilled.
- Run the known plant search immediately when the search screen opens with a prefilled query.
- Preserve free-text scientific name entry when no known plant matches.
- When a known plant is selected, set the scientific name and linked known plant state without changing the plant's user-entered display name.
- Lock the scientific name field when the plant is linked to a known plant.
- Allow the user to unlock the scientific name field by tapping the check mark next to the linked scientific name.

### Non-Goals (explicitly out of scope)

- Fetching suggestions from an external taxonomy or plant identification source.
- Adding a new backend, database table, cache, or sync behavior for known plants.
- Merging hardcoded known plants with a future external source. Once the external source exists, it should become the single source of truth.
- Automatically changing the plant's common name, pests, care tasks, watering settings, or other metadata when a known plant is selected.
- Creating new known plant records from free-text scientific names.
- Supporting fuzzy matching beyond simple case-insensitive matching in version 1.
- Changing the hardcoded known plant list itself as part of this feature.

## User Stories

- As a **plant owner**, I want **scientific name suggestions while I type** so that **I can avoid spelling mistakes and find a known plant faster**.
- As a **plant owner**, I want **the known plant search screen to start with my current scientific name text** so that **I do not have to type the same query twice**.
- As a **plant owner**, I want **selecting a known plant to fill only the scientific name field** so that **my chosen plant name is not overwritten**.
- As a **plant owner**, I want **to keep typing a custom scientific name when no suggestion matches** so that **I can still save uncommon plants**.
- As a **plant owner**, I want **linked scientific names to be locked until I explicitly unlock them** so that **I do not accidentally break the known plant link while editing**.

## Detailed Workflow

_Step-by-step, screen-by-screen. Leave nothing implicit._

### Entry Point

The feature is available from the existing `Add plant` and `Edit plant` screens. The entry point is the existing `Scientific name` text field, which already has a trailing search icon with content description `Search known plants`.

### Flow

**Step 1: Type In Scientific Name Field**

- User sees: The plant form with `Name`, `Scientific name`, the search icon inside the scientific name field, and the rest of the form below.
- Preconditions: The plant is not currently linked to a known plant, or the user has unlocked the linked scientific name field.
- User action: The user types one or more characters into `Scientific name`.
- System response: The app filters the hardcoded known plant list by scientific name and displays up to four suggestions directly below the field.
- Validation: No validation error is shown for partial or unmatched text.
- Edge cases: If the typed text is blank, no inline suggestions are shown. If there are no matches, no suggestion rows are shown and free-text entry remains allowed.

**Step 2: Select Inline Suggestion**

- User sees: Up to four suggestion rows below `Scientific name`. Each row shows the known plant's scientific name as the primary text and the common name as secondary text.
- Preconditions: At least one known plant scientific name matches the typed text.
- User action: The user taps a suggestion row.
- System response: The `Scientific name` field is set to the selected known plant's scientific name, the plant becomes linked to that known plant, inline suggestions collapse, and the field becomes locked.
- Validation: No additional validation is required.
- Edge cases: The existing `Name` field value does not change, even if the selected known plant has a different common name.

**Step 3: Open Known Plant Search With Prefilled Query**

- User sees: The `Scientific name` field with a trailing search icon.
- Preconditions: The scientific name field is unlocked. The user may have typed any text, including partial text or text with no matching inline suggestion.
- User action: The user taps the search icon.
- System response: The app navigates to the existing `Select plant` known plant search screen. The `Search plants` field is prefilled with the current `Scientific name` value from the form, trimmed only for filtering. Search results are filtered immediately when the screen opens.
- Validation: No validation error is shown before navigation.
- Edge cases: If the current scientific name text is blank, the search screen opens with an empty query and shows the default known plant list.

**Step 4: Select Known Plant From Search Screen**

- User sees: The `Select plant` screen with a `Search plants` field and filtered known plant result rows.
- Preconditions: The user opened known plant search from add or edit plant.
- User action: The user taps a known plant result.
- System response: The app returns to the plant form, sets the `Scientific name` field to the selected known plant's scientific name, records that known plant as linked, locks the scientific name field, and preserves all other form values the user had already entered.
- Validation: No validation error is shown.
- Edge cases: The `Name` field remains exactly as the user left it; selecting `Ficus lyrata` does not automatically change the name to `Fiddle-leaf fig`.

**Step 5: Save Free-Text Scientific Name**

- User sees: A scientific name value that may or may not match a known plant.
- Preconditions: The scientific name field is not linked, or the user has unlocked it and typed a custom value.
- User action: The user saves the plant.
- System response: The plant saves with the typed scientific name and no known plant link if no known plant was selected.
- Validation: Existing save validation still applies. This feature does not make scientific name required.
- Edge cases: A free-text value that resembles but does not exactly match a known plant remains unlinked unless the user explicitly selects a known plant.

**Step 6: Unlock Linked Scientific Name**

- User sees: A locked `Scientific name` field with a check mark icon indicating the field is linked to a known plant.
- Preconditions: The plant is linked to a known plant.
- User action: The user taps the check mark next to the scientific name.
- System response: The known plant link is cleared, the scientific name text remains in the field, the field becomes editable, and inline suggestions can appear again as the user edits.
- Validation: No validation error is shown when unlocking.
- Edge cases: Unlocking does not clear the scientific name text. The user can save the same text as free text without a known plant link.

**Completion**

- The flow is complete when the user saves the add or edit form.
- If a known plant remains linked at save time, the plant stores the selected scientific name and known plant identifier.
- If the link was unlocked or no known plant was selected, the plant stores the typed scientific name and no known plant identifier.
- The user returns to the existing post-save destination for add or edit plant.

### State Matrix

_For every screen in this flow, enumerate all possible states._

| Screen | Loading | Empty | Normal | Error | Disabled |
|--------|---------|-------|--------|-------|----------|
| Add plant form | Not applicable for hardcoded suggestions | Blank scientific name shows no suggestions | Typing shows up to four matching suggestions | Not applicable unless existing form save errors occur | Scientific name is disabled only after selecting a known plant |
| Edit plant form | Not applicable for hardcoded suggestions | Blank scientific name shows no suggestions | Existing unlinked value is editable and can show suggestions | Not applicable unless existing form save errors occur | Existing linked value starts locked until the check mark is tapped |
| Known plant search | Not applicable for hardcoded known plants | Prefilled or typed query with no matches shows `No known plants found` | Prefilled query filters result rows immediately | Not applicable for local hardcoded data | Search field remains enabled |

## UI/UX Specifications

### Screen: Add/Edit Plant Form

**Layout description:**
The existing `Add plant` and `Edit plant` screens keep their current top app bar, vertical form layout, and existing `Scientific name` field position. Inline suggestions appear immediately below the scientific name field and above the `Linked to known plant` helper text or the next form section.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Scientific name field | Outlined text field | Below `Name` field | Label `Scientific name` | Accepts free text when unlocked; filters suggestions live as value changes | Editable, locked, focused, unfocused |
| Linked check mark | Icon button | Leading side of `Scientific name` when linked | Content description `Unlock scientific name` | Clears known plant link and makes field editable | Visible when linked, hidden when unlinked |
| Search known plants | Icon button | Trailing side of `Scientific name` | Content description `Search known plants` | Opens `Select plant` with current scientific name prefilled in `Search plants` | Enabled only when unlocked; disabled or hidden when linked |
| Suggestion row | Clickable row/card | Below `Scientific name` | Primary scientific name, secondary common name | Selects known plant, fills scientific name, links plant, locks field | Visible only for non-blank unlocked query with matches |
| Linked helper text | Text | Below field/suggestions | `Linked to known plant` | Informs user the value is linked | Visible when linked |

**Navigation:**

- From: `Add plant` or `Edit plant`.
- To: `Select plant` when the search icon is tapped.
- Transition: Use the existing app navigation transition for known plant search.

**Accessibility:**

- The search icon keeps content description `Search known plants`.
- The linked check mark becomes tappable and uses content description `Unlock scientific name`.
- Suggestion rows announce the scientific name first, then the common name.
- Keyboard focus order is `Name`, `Scientific name`, search icon, suggestion rows, then the next form control.
- Suggestion rows and icon buttons must meet minimum touch target guidance.

### Screen: Select Plant Search

**Layout description:**
The existing `Select plant` screen keeps its top app bar, back button, `Search plants` field, and result list. The only behavior change is that the screen accepts an initial query and applies it immediately.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Back button | Icon button | Top app bar navigation | Content description `Back` | Returns to plant form without selecting a known plant | Enabled |
| Search plants field | Outlined text field | Top of screen content | Label `Search plants` | Starts with initial scientific name query and filters as the user edits | Empty, populated, focused, unfocused |
| Result row | Card/list item | Search results list | Common name and scientific name | Selecting returns to form with known plant link | Normal |
| Empty result text | Text | Below search field | `No known plants found` | Shown when the query has no matches | Visible only when filtered result list is empty |

**Navigation:**

- From: `Add plant` or `Edit plant` search icon.
- To: the same plant form route/draft after selecting a known plant.
- Transition: Use the existing app navigation transition.

**Accessibility:**

- The initial query must be available to screen readers as the current value of `Search plants`.
- Result rows announce common name and scientific name.
- Back returns without mutating the form draft.

## Data Model & State

### Data Sources

- **User input:** The text typed into `Scientific name` and `Search plants`.
- **Local hardcoded data:** The known plant list currently represented by `SampleKnownPlants`.
- **Derived/computed:** Inline suggestions derived from known plants whose `scientificName` matches the current field text.
- **Future external data:** When an external source is added, it replaces the hardcoded known plant list instead of merging with it.

### State Ownership

_Where does each piece of state live?_

- `scientificName`: Plant form state, preserved across navigation through the existing plant draft flow.
- `knownPlantId`: Plant form state, set only when the user selects a known plant and cleared when the user edits or unlocks the field.
- Inline suggestions: Derived UI state from `scientificName`, `knownPlantId`, and the known plant list.
- Initial search query: Passed from the plant form route/draft into `KnownPlantSearchScreen`.
- Search screen query: Local search screen state initialized from the initial query.

### Validation Rules

- `Scientific name`: Optional. Blank values are allowed if existing plant form behavior allows them.
- `Scientific name`: Free text is allowed even when it does not match any known plant.
- `knownPlantId`: Must only be saved when the user explicitly selects a known plant from an inline suggestion or from the full search screen.
- Locked scientific name: User typing is disabled while linked; unlocking is required before manual edits.

### Persistence

- Saved plant scientific name persists according to the existing plant persistence behavior.
- Saved known plant link persists through `knownPlantId` according to existing plant persistence behavior.
- Inline suggestions are not persisted.
- Search query text on the `Select plant` screen is transient unless already represented in the form draft.

## Integration Points

_How this feature connects to existing code._

- **Database:** No schema changes are needed if `Plant.knownPlantId` and `Plant.scientificName` already persist.
- **Navigation:** The route/draft that opens known plant search needs to carry the current scientific name as the initial search query.
- **Shared code:** The plant form should consume the known plant list used by the search screen so inline suggestions and full search stay consistent.
- **Future external source:** The known plant provider should have one active source of truth. Version 1 uses hardcoded data; the later external source replaces that provider.
- **Platform-specific:** No Android-only or iOS-only behavior is required for version 1.

## Acceptance Criteria

_Every item must be a testable, pass/fail statement. No ambiguity._

### AC-1: Add plant shows live suggestions

- **Given** the user is on `Add plant` and the plant is not linked to a known plant
- **When** they type `fic` into `Scientific name`
- **Then** up to four matching known plant suggestions are shown below the field
- **And** each suggestion shows scientific name and common name

### AC-2: Edit plant shows live suggestions

- **Given** the user is on `Edit plant` for an unlinked plant
- **When** they type text that matches known plant scientific names
- **Then** up to four matching known plant suggestions are shown below the field

### AC-3: Suggestions are limited to four

- **Given** more than four known plant scientific names match the typed query
- **When** suggestions are displayed
- **Then** exactly four suggestion rows are shown

### AC-4: Blank query hides inline suggestions

- **Given** the user is editing `Scientific name`
- **When** the field is blank or contains only whitespace
- **Then** no inline suggestions are shown

### AC-5: No matches still allows free text

- **Given** the user types a scientific name that does not match the hardcoded known plant list
- **When** no inline suggestions are shown
- **Then** the user can keep the typed value in the field
- **And** the user can save the plant without selecting a known plant

### AC-6: Inline suggestion links known plant

- **Given** inline suggestions are visible
- **When** the user taps a suggestion
- **Then** `Scientific name` is set to that known plant's scientific name
- **And** the known plant identifier is recorded as linked
- **And** inline suggestions collapse
- **And** the scientific name field becomes locked

### AC-7: Selecting a known plant does not change plant name

- **Given** the `Name` field contains `My living room plant`
- **When** the user selects a known plant suggestion or search result
- **Then** the `Name` field still contains `My living room plant`

### AC-8: Search icon preloads current scientific name

- **Given** the `Scientific name` field contains `Ficus`
- **When** the user taps `Search known plants`
- **Then** the `Select plant` screen opens
- **And** the `Search plants` field contains `Ficus`
- **And** the visible result list is already filtered for `Ficus`

### AC-9: Blank search icon opens default search

- **Given** the `Scientific name` field is blank
- **When** the user taps `Search known plants`
- **Then** the `Select plant` screen opens with an empty `Search plants` field
- **And** the default known plant list is visible

### AC-10: Search result links known plant

- **Given** the user opened `Select plant` from the plant form
- **When** they tap a known plant result
- **Then** the app returns to the same plant form
- **And** `Scientific name` is set to the selected known plant's scientific name
- **And** the known plant identifier is recorded as linked
- **And** all other form values are preserved

### AC-11: Linked field is locked

- **Given** a known plant is linked
- **When** the user focuses or taps the `Scientific name` text area
- **Then** the scientific name text cannot be manually edited
- **And** a check mark icon indicates that the value is linked
- **And** known plant search cannot be used to change the link until the field is unlocked

### AC-12: Check mark unlocks linked field

- **Given** a known plant is linked and `Scientific name` contains `Monstera deliciosa`
- **When** the user taps the check mark next to the scientific name
- **Then** the known plant link is cleared
- **And** `Scientific name` still contains `Monstera deliciosa`
- **And** the field becomes editable

### AC-13: Editing unlocked field clears link

- **Given** the user unlocked a previously linked scientific name
- **When** they change the text manually
- **Then** the plant remains unlinked unless they select a known plant again

### AC-14: Back from search preserves draft

- **Given** the user has typed values into the plant form
- **When** they open `Select plant` and tap `Back` without selecting a plant
- **Then** they return to the plant form
- **And** all previously typed form values are unchanged

### AC-15: Accessibility labels are present

- **Given** a screen reader is enabled
- **When** the user navigates the scientific name field controls
- **Then** the search icon announces `Search known plants`
- **And** the linked check mark announces `Unlock scientific name` when visible

## Implementation Considerations

_Learning-focused guidance. These are suggestions, not mandates._

### Architecture

- This is an addition to the existing plant form and known plant search flow, not a new primary screen.
- Keep suggestion filtering close to the UI or a small shared helper while the data source is hardcoded; move it behind a repository later when the external source replaces the hardcoded list.
- Avoid persisting suggestion state. Persist only the final scientific name and known plant link that the user saves.

### Compose Multiplatform

- A small `LazyColumn`, `Column`, or card list below `OutlinedTextField` can render the four inline suggestions.
- Derive suggestions from the current field value and known plant list; do not store a second mutable copy unless needed for UI behavior.
- The locked field can use `enabled = false` or `readOnly = true`; prefer the option that preserves readable disabled styling and still allows the unlock action to be obvious.
- Use stable keys for suggestion rows based on known plant id.
- Initialize the search screen query from a parameter using `remember(initialQuery) { mutableStateOf(initialQuery) }` so opening search with a draft value filters immediately.

### Kotlin Patterns

- Keep the matching rule simple and explicit for version 1: trim the query, ignore blank queries, match `scientificName` case-insensitively, and take four results.
- A small pure function for suggestion filtering is easy to unit test and can later be replaced by an external taxonomy repository.
- When a known plant is selected, update `scientificName` and `knownPlantId` together so the UI cannot show a locked field without a link.

### Testing Strategy

- Unit test the suggestion filtering rule for blank query, case-insensitive match, no match, and limit of four.
- UI test the add/edit form happy path: type query, tap suggestion, verify field locks and name is preserved.
- UI test search prefill: type query, tap search, verify `Search plants` starts populated and results are filtered.
- UI test unlocking: select known plant, tap check mark, verify field remains populated and becomes editable.

### Learning Opportunities

- This feature is a good exercise in derived Compose state because suggestions can be calculated from existing field state instead of stored separately.
- Passing the current form value into the search route reinforces how navigation state and draft form state work together.
- The lock/unlock behavior is a small example of keeping domain state (`knownPlantId`) and UI affordances (check mark, read-only field) synchronized.

## Open Questions

- None currently.

_Items here should be resolved before the spec moves to `approved` status._
