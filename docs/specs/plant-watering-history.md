# Plant Watering History

> **Status:** approved
> **Created:** 2026-05-10
> **Last updated:** 2026-05-10

## Purpose & Value

Plant owners sometimes forget to record a watering at the moment it happens, or accidentally record one at the wrong time. This feature makes watering history correctable by putting a full timestamp log on the plant edit screen, where users already go to maintain plant details.

- **Problem:** The app can log a plant as watered now, but users cannot see, correct, add, or remove historical watering entries from the plant edit flow.
- **Value:** Users can maintain accurate care history without leaving the plant edit screen.
- **Success metric:** Users can add, edit, and delete watering history from the plant edit screen without needing another flow.

## Goals & Non-Goals

### Goals

- Show the full watering history for an existing plant on its edit screen.
- Allow users to add a watering timestamp for the current plant.
- Allow users to edit the timestamp of an existing watering.
- Allow users to delete an existing watering after confirmation.
- Prevent users from saving future watering timestamps.

### Non-Goals (explicitly out of scope)

- Editing watering intensity.
- Editing watering notes.
- Showing watering charts or analytics.
- Creating recurring watering schedules.
- Adding watering notifications or reminders.
- Supporting bulk edit or bulk delete of watering entries.
- Moving watering history out of the edit screen. It may appear elsewhere later, but it remains on the edit screen.

## User Stories

- As a **plant owner**, I want **to see every watering for a plant on the edit screen** so that **I can verify the care history is accurate**.
- As a **plant owner**, I want **to add a watering at a past date and time** so that **I can record waterings I forgot to log when they happened**.
- As a **plant owner**, I want **to edit the date and time of an existing watering** so that **I can fix accidental incorrect entries**.
- As a **plant owner**, I want **to delete a watering after confirmation** so that **I can remove entries made by mistake**.

## Detailed Workflow

_Step-by-step, screen-by-screen. Leave nothing implicit._

### Entry Point

The user starts from the existing plant list and navigates to the existing edit screen for a saved plant through the app's current edit action. This feature does not add a new route; it extends the existing edit route for plants that already have an `id`.

The watering history section is not shown for a new unsaved plant because watering entries require an existing `plantId`.

### Flow

**Step 1: Open Existing Plant Edit Screen**

- User sees: The current plant edit screen with existing plant fields and a new `Watering History` section below the primary plant fields and pest selection areas.
- Preconditions: The plant already exists and has a stable `id`.
- User action: The user opens the edit screen for a plant.
- System response: The app loads the plant's watering entries and displays them newest first.
- Validation: No user input is validated at this step.
- Edge cases: If the plant has been deleted or cannot be found, the existing missing-plant behavior should remain unchanged.

**Step 2: Review Watering History**

- User sees: A `Watering History` heading, an `Add watering` action, and either a list of watering rows or an empty state.
- Preconditions: The edit screen is showing an existing plant.
- User action: The user scrolls through the watering history.
- System response: No data changes occur while browsing.
- Validation: Not applicable.
- Edge cases: If the plant has no waterings, show `No watering history yet.` instead of an empty list.

**Step 3: Add Watering**

- User sees: A date/time editor opened from `Add watering`, using picker controls rather than free-form text entry.
- Preconditions: The edit screen is showing an existing plant.
- User action: The user chooses a date and time, then chooses `Save`.
- System response: The app creates a new watering for the current plant and returns to the edit screen with the new entry in the correct sorted position.
- Validation: Date and time are required. The timestamp must be now or in the past.
- Edge cases: If the selected timestamp is in the future, keep the editor open and show `Watering time cannot be in the future.`

**Step 4: Edit Watering**

- User sees: The same date/time editor populated with the selected watering's timestamp, using picker controls rather than free-form text entry.
- Preconditions: The selected watering belongs to the plant currently being edited.
- User action: The user changes the date and/or time, then chooses `Save`.
- System response: The app updates the existing watering timestamp and returns to the edit screen with the entry moved if its sorted position changed.
- Validation: Date and time are required. The timestamp must be now or in the past.
- Edge cases: If the selected timestamp is in the future, keep the editor open and show `Watering time cannot be in the future.`

**Step 5: Delete Watering**

- User sees: A delete action on each watering row.
- Preconditions: The selected watering belongs to the plant currently being edited.
- User action: The user chooses the delete action for a watering.
- System response: The app shows a confirmation dialog.
- Validation: Not applicable before showing the dialog.
- Edge cases: If the watering no longer exists by the time the user confirms, close the dialog and leave the history in its latest state.

**Step 6: Confirm Delete**

- User sees: A confirmation dialog with title `Delete watering?`, body `This watering entry will be permanently removed.`, confirm action `Delete`, and cancel action `Cancel`.
- Preconditions: A watering delete confirmation dialog is open.
- User action: The user chooses `Delete`.
- System response: The app removes the watering and closes the dialog.
- Validation: Not applicable.
- Edge cases: If the user chooses `Cancel` or dismisses the dialog, no watering data changes.

**Completion**

- The user is done after the edit screen shows the expected watering history.
- The user returns to their previous context using the existing edit screen navigation behavior.
- There is no undo period. Deletion uses confirmation instead.

### State Matrix

| Screen | Loading | Empty | Normal | Error | Disabled |
|--------|---------|-------|--------|-------|----------|
| Plant edit screen watering section | Use the edit screen's existing loading behavior if present; otherwise render with current in-memory state | Show `No watering history yet.` below `Watering History` | Show waterings newest first with date, time, and delete action | If history cannot be loaded, show `Watering history could not be loaded.` and keep plant editing available | `Add watering` disabled only while a save operation is in progress, if async persistence is introduced |
| Add/edit watering date-time editor | Not expected for in-memory state | Not applicable | Show selected date/time picker controls, `Save`, and `Cancel` | Show `Watering time cannot be in the future.` for future timestamps | `Save` disabled or blocked while timestamp is invalid |
| Delete watering confirmation dialog | Not expected | Not applicable | Show title, body, `Delete`, and `Cancel` | If deletion fails, keep or restore the row and show an error using the app's existing error pattern | `Delete` disabled only while deletion is in progress, if async persistence is introduced |

## UI/UX Specifications

### Screen: Plant Edit Screen

**Layout description:**

The existing plant edit screen remains a vertically scrollable form. Add a `Watering History` section after the core plant identity and pest editing controls, unless the existing layout has a more natural lower section for care-related information. The section should be visible only when editing an existing plant.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Watering History heading | Text | In the edit form content | `Watering History` | Identifies the section | Visible for existing plants only |
| Add watering action | Button or text button | Near the section heading or directly below it | `Add watering` | Opens the date/time editor with current timestamp | Enabled normally; disabled while saving if async persistence is introduced |
| Empty history message | Text | Below section heading/action | `No watering history yet.` | Shown when the plant has no waterings | Visible only when history list is empty |
| Watering row | Row/list item | Below heading/action, newest first | Date and time for the watering | Selecting the row opens edit mode for that watering | Visible for each watering |
| Watering row delete action | Icon button | Trailing side of each watering row | Content description: `Delete watering` | Opens delete confirmation dialog | Enabled normally; disabled while deleting if async persistence is introduced |

**Navigation:**

- From: Existing plant list edit entry point.
- To: Existing plant edit screen route.
- Transition: Use the app's existing navigation transition.

**Accessibility:**

- The delete icon must have content description `Delete watering`.
- Each watering row should expose its date/time as readable text for TalkBack.
- The `Add watering` action should be reachable after the `Watering History` heading in focus order.
- Touch targets for row actions should meet the Material minimum target size.

### Screen: Add/Edit Watering Date-Time Editor

**Layout description:**

Use a dialog or sheet consistent with the app's existing Material 3 patterns. The editor should use date and time picker controls instead of free-form text fields because pickers are easier for users to operate correctly on mobile and avoid date-format ambiguity.

Recommended interaction:

- The editor opens as an `Add watering` or `Edit watering` dialog/sheet.
- The body shows two tappable rows or fields: `Date` and `Time`.
- Tapping `Date` opens a calendar-style date picker.
- Tapping `Time` opens a clock-style or spinner-style time picker.
- The user returns to the editor after choosing date or time, then chooses `Save`.
- Future timestamps are blocked at save time, and future dates/times should be disabled in the picker where the component supports it.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Editor title | Text | Dialog/sheet header | `Add watering` or `Edit watering` | Communicates mode | Add mode or edit mode |
| Date input | Date picker trigger | Dialog/sheet body | `Date` | Opens a calendar-style date picker | Required; invalid if combined timestamp is future |
| Time input | Time picker trigger | Dialog/sheet body | `Time` | Opens a clock-style or spinner-style time picker | Required; invalid if combined timestamp is future |
| Validation message | Text | Near inputs | `Watering time cannot be in the future.` | Explains why save cannot complete | Visible only when timestamp is future |
| Cancel action | Button/text button | Dialog/sheet actions | `Cancel` | Closes editor without saving | Always enabled |
| Save action | Button | Dialog/sheet actions | `Save` | Adds or updates watering timestamp | Blocked or disabled while timestamp is invalid |

**Navigation:**

- From: `Add watering` action or selecting a watering row.
- To: Returns to the same plant edit screen after save or cancel.
- Transition: Use the app's existing dialog or sheet behavior.

**Accessibility:**

- Date and time controls must have clear labels.
- Validation text should be announced when it appears.
- Initial focus should move to the first editable date/time field when the editor opens.

### Screen: Delete Watering Confirmation Dialog

**Layout description:**

Use a standard Material confirmation dialog. Confirmation is preferred over undo because users are unlikely to delete many watering entries in a row.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Dialog title | Text | Dialog header | `Delete watering?` | Communicates destructive action | Always visible |
| Dialog body | Text | Dialog body | `This watering entry will be permanently removed.` | Explains consequence | Always visible |
| Cancel action | Text button | Dialog actions | `Cancel` | Closes dialog without deleting | Always enabled |
| Delete action | Button/text button | Dialog actions | `Delete` | Deletes watering and closes dialog | Enabled normally; disabled while deletion is in progress if async persistence is introduced |

**Navigation:**

- From: Delete action on a watering row.
- To: Returns to the same plant edit screen after confirm, cancel, or dismiss.
- Transition: Use the app's existing dialog behavior.

**Accessibility:**

- Dialog focus should be trapped while open.
- `Cancel` should be reachable before `Delete` in focus order where practical.
- The destructive action should be clearly labeled as `Delete`.

## Data Model & State

### Data Sources

- **User input:** Date and time for a watering timestamp.
- **Local database:** No schema change is required for the current in-memory implementation. If persistence is added later, use the existing `Watering` concept and persist `id`, `plantId`, and `wateredAt` at minimum.
- **Derived/computed:** Plant-specific watering history sorted newest first.

### State Ownership

_Where does each piece of state live?_

- Watering records: `PlantListViewModel` / app state, consistent with existing `waterings` ownership.
- Selected add/edit timestamp while the editor is open: local composable state inside the date/time editor until saved.
- Selected watering pending deletion: local composable state in the edit screen or hoisted callback state, cleared after confirm/cancel.

### Validation Rules

- Watering date/time: Required -> keep editor open if missing.
- Watering timestamp: Must be less than or equal to current time -> `Watering time cannot be in the future.`
- Watering ownership: Edited or deleted watering must belong to the plant currently being edited -> ignore or reject mismatched entries.

### Persistence

- Current app behavior is in-memory only, so waterings follow the app's existing restart behavior.
- This feature should not introduce persistence changes by itself.
- If persistence is added later, added/edited/deleted waterings should survive app restart and device reboot through the same persistence layer as other plant data.

## Integration Points

_How this feature connects to existing code._

- **Database:** No database migration is required for the current in-memory app state. If a database-backed watering table exists later, this feature will need insert, update timestamp, delete, and query-by-plant operations.
- **Navigation:** No new route is required. Extend the existing edit plant route to pass plant-specific waterings and watering callbacks.
- **Shared code:** Reuse `Watering`, `PlantWateringSummary`, and existing watering list state where appropriate. Add support for updating and deleting waterings.
- **Platform-specific:** No Android-only or iOS-only behavior is expected. The UI should remain in shared Compose Multiplatform code.

## Acceptance Criteria

_Every item must be a testable, pass/fail statement. No ambiguity._

### AC-1: Existing plant edit screen shows watering history section

- **Given** an existing plant is open for editing
- **When** the edit screen is displayed
- **Then** a section titled `Watering History` is visible
- **And** an action labeled `Add watering` is visible

### AC-2: New plant form does not show watering history

- **Given** the user is creating a new unsaved plant
- **When** the plant form is displayed
- **Then** the `Watering History` section is not shown

### AC-3: Existing waterings are sorted newest first

- **Given** a plant has multiple watering entries with different timestamps
- **When** the user opens that plant's edit screen
- **Then** the watering rows are ordered from newest timestamp to oldest timestamp

### AC-4: Empty watering history shows empty state

- **Given** a plant has no watering entries
- **When** the user opens that plant's edit screen
- **Then** the text `No watering history yet.` is shown in the `Watering History` section

### AC-5: Add watering opens date-time editor

- **Given** an existing plant edit screen is open
- **When** the user chooses `Add watering`
- **Then** a date/time editor opens
- **And** the editor title is `Add watering`
- **And** the editor defaults to the current date and time

### AC-6: Add past watering saves entry

- **Given** the add watering editor is open
- **When** the user selects a timestamp in the past and chooses `Save`
- **Then** a watering is added for the current plant
- **And** the editor closes
- **And** the new watering appears in the history at the correct sorted position

### AC-7: Add future watering is blocked

- **Given** the add watering editor is open
- **When** the user selects a future timestamp and chooses `Save`
- **Then** no watering is added
- **And** the editor remains open
- **And** the message `Watering time cannot be in the future.` is shown

### AC-8: Edit watering opens populated date-time editor

- **Given** an existing plant edit screen shows at least one watering row
- **When** the user selects a watering row
- **Then** a date/time editor opens
- **And** the editor title is `Edit watering`
- **And** the editor is populated with the selected watering's timestamp

### AC-9: Edit past or current watering timestamp saves update

- **Given** the edit watering editor is open
- **When** the user changes the timestamp to the current time or a past time and chooses `Save`
- **Then** the existing watering is updated
- **And** no duplicate watering is created
- **And** the watering row appears at the correct sorted position

### AC-10: Edit future watering timestamp is blocked

- **Given** the edit watering editor is open
- **When** the user selects a future timestamp and chooses `Save`
- **Then** the existing watering is not changed
- **And** the editor remains open
- **And** the message `Watering time cannot be in the future.` is shown

### AC-11: Cancel add or edit does not change history

- **Given** the add or edit watering editor is open
- **When** the user chooses `Cancel` or dismisses the editor
- **Then** the editor closes
- **And** no watering is added or changed

### AC-12: Delete watering asks for confirmation

- **Given** an existing plant edit screen shows at least one watering row
- **When** the user chooses that row's delete action
- **Then** a confirmation dialog is shown
- **And** the dialog title is `Delete watering?`
- **And** the dialog body is `This watering entry will be permanently removed.`
- **And** actions labeled `Cancel` and `Delete` are visible

### AC-13: Confirm delete removes watering

- **Given** the delete watering confirmation dialog is open
- **When** the user chooses `Delete`
- **Then** the selected watering is removed
- **And** the dialog closes
- **And** the removed watering no longer appears in the history

### AC-14: Cancel delete preserves watering

- **Given** the delete watering confirmation dialog is open
- **When** the user chooses `Cancel` or dismisses the dialog
- **Then** the selected watering remains unchanged
- **And** the dialog closes

### AC-15: Accessibility labels are present

- **Given** the plant edit screen watering history is visible
- **When** accessibility services inspect the UI
- **Then** each delete action has content description `Delete watering`
- **And** each watering row exposes its date and time as readable text

## Implementation Considerations

_Learning-focused guidance. These are suggestions, not mandates._

### Architecture

- This is an addition to the existing shared Compose edit screen, currently represented by `PlantFormScreen`.
- Keep watering mutation logic in the ViewModel layer rather than inside composables. The UI should call callbacks such as add, update, and delete.
- Add or expose a query for `wateringsForPlant(plantId)` sorted newest first so the UI does not duplicate sorting rules.

### Compose Multiplatform

- A `LazyColumn` or existing scrollable form can render the watering rows. If `PlantFormScreen` already owns scrolling, avoid nested vertical scroll containers unless necessary.
- Use state hoisting for the selected watering being edited or deleted. The editor/dialog can own temporary input state until the user saves.
- Use Material 3 dialog patterns consistent with the rest of the app for add/edit and delete confirmation.
- Prefer shared Compose Multiplatform date/time picker controls if available in the current Material 3 dependency. If the time picker is not available or is awkward on a target, use the simplest shared custom picker that still avoids free-form date/time text input.
- Prefer deriving displayed rows from immutable state passed into the composable instead of mutating local lists.

### Kotlin Patterns

- Use `copy()` when updating a `Watering` data class instance.
- Prefer small helper functions for pure date/time formatting if the logic would otherwise clutter UI code.
- Keep validation as a pure function where practical, such as checking whether a selected `Instant` is greater than the current time.
- Inject or pass the current time source in tests if direct clock usage would make timestamp validation tests flaky.

### Testing Strategy

- Unit test sorting waterings newest first for a plant.
- Unit test add, update, and delete behavior in the ViewModel or state holder.
- Unit test future timestamp validation.
- Add Compose UI coverage for the critical path if the project has UI test infrastructure: open edit screen, add past watering, verify row appears, delete with confirmation.

### Learning Opportunities

- This feature is a good exercise in state hoisting: the screen owns which dialog is open, while the ViewModel owns durable watering data.
- The date/time editor is a good place to practice validation feedback in Compose without mixing validation rules into layout code.

## Open Questions

- No open questions currently.
