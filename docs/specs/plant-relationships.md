# Plant Relationships

> **Status:** approved
> **Created:** 2026-05-10
> **Last updated:** 2026-05-10

## Purpose & Value

Plant owners often propagate plants by taking cuttings, separating offshoots, collecting corms, dividing plants, or growing seeds. Once those propagated plants become separate entries in the collection, the app currently has no way to preserve where they came from. This feature records directional relationships between plants so the user can look back at a plant's lineage, navigate between related plants, and understand the history of a plant even several generations after the original plant was acquired.

- **Problem:** A propagated plant becomes a separate plant entry, but the app does not preserve the connection to its parent plant or plants.
- **Value:** Users can keep a fun, historical lineage record and navigate between related plants, even if some related plants later become inactive or die.
- **Success metric:** Users can create, view, edit, and remove plant relationships locally, and can navigate from a plant detail page to each immediate parent or child plant.

## Goals & Non-Goals

### Goals

- Allow users to assign a relationship while creating a new plant.
- Allow users to add a relationship later to an existing plant.
- Support a fixed list of relationship types: `Cutting`, `Offshoot`, `Corm`, `Division`, `Seed`, and `Unknown`.
- Require one selected parent for single-parent relationship types and two selected parents for `Seed`.
- Store relationships as one row per parent plant. A `Seed` relationship creates two relationship records with the same child plant, type, date, and notes.
- Allow relationship date and notes to be saved and edited.
- Show immediate parent and child relationships on the plant detail page in a read-only timeline.
- Allow navigation from the plant detail timeline to related plant detail pages.
- Include inactive or dead plants in parent search results once plant status exists.
- Delete relationship rows when a plant is permanently deleted, while preserving the related plants.

### Non-Goals (explicitly out of scope)

- Implementing plant status values such as active, dead, diseased, given away, or inactive. This spec references those statuses only as future functionality.
- Showing grandparents, grandchildren, siblings, cousins, or a full family tree.
- Supporting custom relationship types.
- Supporting relationship images, attachments, or media.
- Exporting, sharing, or syncing lineage data.
- Automatically creating plant records from a relationship.
- Tracking propagation progress, rooting stages, or care tasks for a propagation attempt.
- Supporting more than two parents for a relationship.

## User Stories

- As a **plant owner**, I want **to mark a new plant as coming from another plant** so that **I can remember its origin**.
- As a **plant owner**, I want **to support two parents for seed-grown plants** so that **I can record both parent plants when I make my own crosses**.
- As a **plant owner**, I want **to navigate from a plant to its immediate parents and children** so that **I can follow lineage through the collection**.
- As a **plant owner**, I want **to edit relationship type, date, notes, and parents** so that **I can correct mistakes later**.
- As a **plant owner**, I want **related plants to remain navigable even if they later become inactive or dead** so that **historical lineage is preserved**.

## Detailed Workflow

_Step-by-step, screen-by-screen. Leave nothing implicit._

### Entry Point

The primary entry point is the existing plant create/edit form. When the user creates a new plant, the form includes an optional `Plant Relationship` section at the bottom of the form where they can choose whether this plant came from another plant. When the user edits an existing plant, the same bottom section lets them add, edit, or remove the relationship for that plant.

The secondary entry point is the plant detail page. It shows a read-only `Lineage` timeline containing the plant's immediate parents and immediate children. Selecting a related plant navigates to that plant's detail page. Editing is not available from the timeline in version 1.

### Flow

**Step 1: Create New Plant With Optional Relationship**

- User sees: The existing new plant form with plant identity fields and a `Plant Relationship` section below the main plant details. The section starts collapsed or empty with an `Add relationship` action.
- Preconditions: The user is creating a new plant entry.
- User action: The user chooses `Add relationship`.
- System response: The form expands the relationship editor and defaults the relationship type to `Cutting` unless the app already has a better default pattern.
- Validation: No relationship validation runs until the user tries to save the plant with a relationship editor active.
- Edge cases: If the user never adds a relationship, the plant saves with no relationship.

**Step 2: Choose Relationship Type**

- User sees: A relationship type selector labeled `Relationship type` with fixed options `Cutting`, `Offshoot`, `Corm`, `Division`, `Seed`, and `Unknown`.
- Preconditions: The relationship editor is visible.
- User action: The user chooses a relationship type.
- System response: The required parent count updates immediately. `Seed` requires two parent selections. All other relationship types require one parent selection. Changing the relationship type clears any previously selected parents.
- Validation: The selected relationship type must be one of the fixed options.
- Edge cases: If the user changes the relationship type after selecting parents, the parent selection is cleared and the app shows a brief inline note `Choose parent plants again for this relationship type.`

**Step 3: Open Parent Plant Search**

- User sees: A `Choose parent plant` action for one-parent relationship types or a `Choose parent plants` action for `Seed`.
- Preconditions: A relationship type has been selected.
- User action: The user taps the choose action.
- System response: The app navigates to a full-screen parent plant search page. The page receives a maximum selection count of `1` for one-parent relationship types or `2` for `Seed`.
- Validation: The current child plant cannot be selected as its own parent when editing an existing plant.
- Edge cases: If there are no other plants available, the search page shows an empty state and the confirm button remains disabled.

**Step 4: Search And Select Parent Plants**

- User sees: A parent plant search page with top app bar title `Choose parent plant` or `Choose parent plants`, a search field labeled `Search plants`, a list of matching plants, selected-state indicators, and a bottom `Confirm selection` button.
- Preconditions: The user is on the parent plant search page.
- User action: The user searches by plant name or scientific name, selects one or two plants depending on the relationship type, then taps `Confirm selection`.
- System response: The app returns to the plant form and displays the selected parent plant or plants in the relationship editor.
- Validation: `Confirm selection` is disabled until the required number of parents is selected.
- Edge cases: If the user reaches the maximum selection count and taps another plant, the app either replaces the oldest selection for one-parent mode or shows `Remove a selected plant before choosing another.` for two-parent mode.

**Step 5: Enter Relationship Date And Notes**

- User sees: A `Relationship date` date picker trigger and a multiline `Notes` text field with placeholder `Add notes about this relationship`.
- Preconditions: The relationship editor is visible.
- User action: The user chooses a date and optionally enters notes.
- System response: The selected date and notes remain in the form state.
- Validation: Relationship date is required. Notes are optional text only.
- Edge cases: If no date is chosen, the app defaults to today's date when the relationship editor is first opened. Future dates are blocked with `Relationship date cannot be in the future.`

**Step 6: Save Plant With Relationship**

- User sees: The normal plant form save action.
- Preconditions: The plant form is valid and the relationship editor is either absent or valid.
- User action: The user taps `Save`.
- System response: The app saves the plant and saves one relationship record per selected parent, where the current plant is the child. A `Seed` relationship saves two relationship records with matching type, date, and notes.
- Validation: If a relationship is active, relationship type, required parent selections, and relationship date are required.
- Edge cases: If the relationship cannot be saved, keep the user on the form and show `Relationship could not be saved. Try again.` without losing entered plant data.

**Step 7: Add Relationship To Existing Plant**

- User sees: The existing plant edit form with the `Plant Relationship` section. If the plant has no relationship, the section shows `No parent relationship set.` and an `Add relationship` action.
- Preconditions: The plant already exists and does not currently have a parent relationship.
- User action: The user taps `Add relationship`, fills out the relationship editor, and taps `Save`.
- System response: The app saves the relationship and returns to the previous context using existing edit form behavior.
- Validation: Same as creating a new plant with a relationship.
- Edge cases: The parent search includes inactive or dead plants when plant status exists, because lineage needs historical records.

**Step 8: Edit Existing Relationship**

- User sees: The existing plant edit form with a populated `Plant Relationship` section showing relationship type, parent plants, relationship date, notes, and actions `Edit relationship` and `Remove relationship`.
- Preconditions: The plant already has a parent relationship.
- User action: The user taps `Edit relationship`, changes one or more fields, then taps `Save` on the plant form.
- System response: Before saving, the app shows a confirmation dialog summarizing each changed relationship field with old and new values.
- Validation: The edited relationship must still satisfy the selected relationship type's parent count and date rules.
- Edge cases: If no relationship fields changed, no relationship change confirmation is shown.

**Step 9: Confirm Relationship Edits**

- User sees: A confirmation dialog with title `Save relationship changes?`, body text listing each changed field, cancel action `Cancel`, and confirm action `Save changes`.
- Preconditions: The user is saving an existing plant after changing relationship fields.
- User action: The user taps `Save changes`.
- System response: The app saves the relationship edits and returns to the edit flow's normal saved state.
- Validation: Not applicable inside the dialog because validation already passed before it opened.
- Edge cases: If the user taps `Cancel` or dismisses the dialog, the user returns to the edit form with unsaved edits still visible.

**Step 10: Remove Relationship From Existing Plant**

- User sees: The populated `Plant Relationship` section and a `Remove relationship` action.
- Preconditions: The plant already has a parent relationship.
- User action: The user taps `Remove relationship`.
- System response: The app shows a confirmation dialog with title `Remove relationship?`, body `This removes the lineage link from this plant. The related plants will not be deleted.`, confirm action `Remove`, and cancel action `Cancel`.
- Validation: Not applicable.
- Edge cases: If the user confirms, only the relationship row is removed. No plant records are deleted.

**Step 11: View Lineage Timeline**

- User sees: The plant detail page with a `Lineage` section showing immediate parents above or before the current plant and immediate children below or after the current plant. Each row shows plant thumbnail, status badge on the thumbnail when status exists, plant name, relationship type, and relationship date.
- Preconditions: The user is viewing a plant detail page.
- User action: The user reviews the timeline or taps a related plant row.
- System response: Tapping a related plant navigates to that plant's detail page. No relationship editing is available from this timeline.
- Validation: Not applicable.
- Edge cases: If the plant has no parents and no children, show `No lineage recorded yet.`

**Step 12: Permanently Delete A Plant With Relationships**

- User sees: The normal permanent delete confirmation for a plant. If the plant has any parent or child relationships, the dialog additionally states `This plant has lineage relationships. Those relationship links will be deleted, but the related plants will remain.`
- Preconditions: The app supports permanent plant deletion and the selected plant has one or more relationship rows.
- User action: The user confirms permanent deletion.
- System response: The app deletes the plant and all relationship rows where that plant is either child or parent. Related plant records remain unchanged.
- Validation: Not applicable.
- Edge cases: If deletion fails, keep the plant and relationship rows unchanged and show the app's normal deletion error pattern.

**Completion**

- The user is done after the plant saves successfully with the intended relationship, or after the detail page lineage timeline shows the expected related plants.
- The user returns to their previous context using the app's existing save and navigation behavior.
- There is no undo period for relationship removal or plant deletion. Both use confirmation instead.

### State Matrix

_For every screen in this flow, enumerate all possible states._

| Screen | Loading | Empty | Normal | Error | Disabled |
|--------|---------|-------|--------|-------|----------|
| Plant create/edit relationship section | Use the form's existing loading behavior if present | Show `No parent relationship set.` for existing plants without a relationship | Show selected type, parent plants, date, notes, and edit/remove actions | Show `Relationship could not be saved. Try again.` if persistence fails | Save blocked while required relationship fields are missing |
| Parent plant search page | Show a progress indicator if plants are loaded asynchronously | Show `No plants found.` for an empty query result; show `No other plants available.` when no selectable plants exist | Show searchable list of plants with selected-state indicators and bottom `Confirm selection` button | Show `Plants could not be loaded. Try again.` with retry action | `Confirm selection` disabled until required parent selection count is selected |
| Relationship edit confirmation dialog | Not expected | Not applicable | Show changed fields with old and new values, `Cancel`, and `Save changes` | If save fails after confirmation, close or keep dialog according to app error pattern and show save error on form | `Save changes` disabled only while save is in progress if async persistence is introduced |
| Remove relationship confirmation dialog | Not expected | Not applicable | Show title, consequence text, `Cancel`, and `Remove` | If removal fails, keep relationship visible and show an error using the app's existing pattern | `Remove` disabled only while removal is in progress if async persistence is introduced |
| Plant detail lineage timeline | Use the detail page's existing loading behavior if present | Show `No lineage recorded yet.` | Show immediate parents and children with thumbnails, names, relationship type, date, and status badge when status exists | Show `Lineage could not be loaded.` while keeping the rest of the detail page available | Timeline rows disabled only while navigating if the app prevents repeated navigation taps |
| Permanent delete confirmation dialog | Not expected | Not applicable | Show normal delete confirmation plus lineage warning when relationships exist | If deletion fails, leave all data unchanged and show the app's normal deletion error | Confirm delete disabled only while deletion is in progress if async persistence is introduced |

## UI/UX Specifications

### Screen: Plant Create/Edit Form

**Layout description:**

The existing plant form remains a vertically scrollable form. Add a `Plant Relationship` section at the bottom of the form because lineage is useful historical context but less important than the core plant values users edit most often. The section is optional during creation and editable for existing plants.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Section heading | Text | In form content | `Plant Relationship` | Identifies the lineage section | Always visible on create/edit form |
| Empty relationship text | Text | Below section heading | `No parent relationship set.` | Explains that no lineage is set | Visible only for existing plants with no relationship |
| Add relationship action | Button or text button | Below empty text or heading | `Add relationship` | Opens relationship editor | Enabled normally; hidden when editor or existing relationship is visible |
| Relationship type selector | Dropdown or segmented/list selector | Relationship editor | `Relationship type` | Lets user choose one fixed type | Required when relationship editor is active |
| Parent picker action | Button/list item | Relationship editor | `Choose parent plant` or `Choose parent plants` | Navigates to parent plant search | Enabled after relationship type is selected |
| Selected parent row | Row/list item | Relationship editor | Parent plant name | Shows selected parent plant | One row for one-parent types; two rows for `Seed` |
| Relationship date field | Date picker trigger | Relationship editor | `Relationship date` | Opens date picker | Required; defaults to today's date |
| Notes field | Multiline text field | Relationship editor | Placeholder `Add notes about this relationship` | Stores optional relationship notes | Optional; plain text only |
| Edit relationship action | Button or text button | Existing relationship summary | `Edit relationship` | Makes relationship fields editable | Visible for existing saved relationships |
| Remove relationship action | Text button | Existing relationship summary | `Remove relationship` | Opens remove relationship confirmation | Visible for existing saved relationships |
| Save action | Existing form action | Existing form position | Existing save label | Saves plant and relationship | Blocked if active relationship is invalid |

**Navigation:**

- From: Existing plant list, plant detail page, or any existing create/edit entry point.
- To: Parent plant search page when choosing parents.
- Transition: Use the app's existing navigation transition for full-screen form-to-search navigation.

**Accessibility:**

- Relationship type selector must expose the selected value to TalkBack.
- Parent picker action should announce whether one or two parents are required.
- Selected parent rows should read the plant name and whether the row is `Parent 1` or `Parent 2`.
- `Remove relationship` must be clearly labeled as destructive.
- All interactive elements should meet Material minimum touch target sizes.

### Screen: Parent Plant Search Page

**Layout description:**

Use a full-screen search page rather than an inline picker, because the plant list may become large and should include inactive or dead plants in the future. The screen has a top app bar, search field, scrollable result list, and a fixed bottom confirmation action.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Top app bar title | Text | Top app bar | `Choose parent plant` or `Choose parent plants` | Communicates selection mode | Singular for max 1; plural for max 2 |
| Back action | Icon button | Top app bar leading icon | Content description `Go back` | Returns to plant form without applying new selection | Enabled normally |
| Search field | Text field | Below top app bar | Label `Search plants` | Filters by plant name and scientific name | Empty query shows all selectable plants |
| Result row thumbnail | Image | Leading side of each result row | Content description uses plant name | Helps identify plant | Placeholder if no photo exists |
| Result row text | Text | Center of each result row | Plant name, optional scientific name | Tapping row toggles selection | Normal, selected, or disabled for current child plant |
| Selected indicator | Icon or checkbox | Trailing side of row | Content description `Selected` | Shows selected state | Visible when selected |
| Empty result message | Text | List content area | `No plants found.` | Explains no matches | Visible when search returns no rows |
| Confirm selection action | Button | Fixed bottom bar | `Confirm selection` | Returns selected parents to plant form | Disabled until required selection count is reached |

**Navigation:**

- From: Plant create/edit form parent picker action.
- To: Returns to the same plant form with selected parent plant IDs after confirmation.
- Transition: Use standard full-screen navigation. Back returns without applying unconfirmed changes.

**Accessibility:**

- Search field should receive focus when the screen opens if that matches platform expectations.
- Result rows should announce plant name, scientific name when present, and selected state.
- The bottom button should announce the current selection requirement, for example `Confirm selection, 1 of 2 selected`.
- Disabled current-child row should explain `This plant cannot be its own parent`.

### Screen: Relationship Edit Confirmation Dialog

**Layout description:**

Use a Material confirmation dialog when saving edits to an existing relationship. The dialog protects against accidental lineage changes by showing exactly what will change before the save is committed.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Dialog title | Text | Dialog header | `Save relationship changes?` | Communicates confirmation purpose | Always visible |
| Changed field list | Text/list | Dialog body | One row per changed field, such as `Type: Cutting -> Corm` | Shows old and new values | Only changed fields are listed |
| Cancel action | Text button | Dialog actions | `Cancel` | Returns to form with unsaved edits still visible | Always enabled |
| Save changes action | Button | Dialog actions | `Save changes` | Saves relationship edits | Disabled only while saving if async persistence is introduced |

**Navigation:**

- From: Saving the plant edit form after changing existing relationship fields.
- To: Returns to plant edit flow after confirm, cancel, or dismiss.
- Transition: Use the app's existing dialog behavior.

**Accessibility:**

- Dialog focus should be trapped while open.
- Changed fields should be readable as text, not only visual diff styling.
- `Cancel` should be reachable before `Save changes` where practical.

### Screen: Plant Detail Lineage Timeline

**Layout description:**

Add a `Lineage` section to the plant detail page. The section is read-only in version 1. It shows only immediate parents and immediate children, not deeper ancestry or related branches. The layout can be a vertical timeline or grouped list, as long as parent rows and child rows are visually distinguishable.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Section heading | Text | Detail page content | `Lineage` | Identifies related plant history | Visible on plant detail page |
| Empty lineage text | Text | Below heading | `No lineage recorded yet.` | Explains no relationships exist | Visible only when no parent or child relationships exist |
| Parent group label | Text | Timeline content | `Parents` | Labels immediate parent rows | Hidden if no parents exist |
| Child group label | Text | Timeline content | `Children` | Labels immediate child rows | Hidden if no children exist |
| Related plant row | Row/list item | Timeline content | Plant name, relationship type, relationship date | Navigates to related plant detail page | Enabled normally; read-only |
| Related plant thumbnail | Image | Leading side of row | Content description uses plant name | Helps identify plant | Shows placeholder if no photo exists |
| Status badge | Pill/badge overlay | On top of thumbnail | Future status label, such as `Active`, `Alive`, `Dead`, or `Diseased` | Shows status once plant status exists | Hidden until status exists |

**Navigation:**

- From: Plant detail page.
- To: Related plant detail page when a related plant row is tapped.
- Transition: Use the app's existing detail-to-detail navigation behavior.

**Accessibility:**

- Related plant rows should announce plant name, relationship direction, relationship type, date, and future status when present.
- Thumbnail badges must not be the only way status is conveyed; status should also be present in accessible row text once status exists.
- Timeline rows should meet Material minimum touch target sizes.

### Screen: Remove Relationship Confirmation Dialog

**Layout description:**

Use a standard Material confirmation dialog because removing a relationship deletes lineage history for the current plant, even though no plant records are deleted.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Dialog title | Text | Dialog header | `Remove relationship?` | Communicates destructive action | Always visible |
| Dialog body | Text | Dialog body | `This removes the lineage link from this plant. The related plants will not be deleted.` | Explains consequence | Always visible |
| Cancel action | Text button | Dialog actions | `Cancel` | Closes dialog without removing relationship | Always enabled |
| Remove action | Button/text button | Dialog actions | `Remove` | Removes relationship and closes dialog | Disabled only while removal is in progress if async persistence is introduced |

**Navigation:**

- From: `Remove relationship` action in the plant edit form.
- To: Returns to the same plant edit form after confirm, cancel, or dismiss.
- Transition: Use the app's existing dialog behavior.

**Accessibility:**

- Dialog focus should be trapped while open.
- The destructive action should be clearly labeled as `Remove`.
- `Cancel` should be reachable before `Remove` where practical.

## Data Model & State

### Data Sources

- **User input:** Relationship type, selected parent plant IDs, relationship date, and optional relationship notes.
- **Local database:** A new plant relationship entity should be persisted locally once app persistence exists. Current implementation may start in memory if that matches the rest of the app state.
- **Derived/computed:** Parent timeline rows are relationship records where the current plant is the child. Child timeline rows are relationship records where the current plant is the parent. Required parent selection count is derived from relationship type. A `Seed` UI relationship is derived from two stored relationship records with the same child, type, date, and notes.

### State Ownership

_Where does each piece of state live?_

- Relationship editor draft: Plant form ViewModel or screen state, consistent with the existing plant form architecture.
- Parent search query and temporary selections: Parent search screen/ViewModel state until `Confirm selection` is tapped.
- Saved relationship records: Repository/data source layer once persistence exists; current in-memory state may live beside existing plant collection state.
- Lineage timeline UI state: Plant detail ViewModel or equivalent screen state derived from plants plus relationship records.
- Relationship edit diff: Plant edit ViewModel or form state, derived by comparing the original saved relationship with the edited draft.

### Validation Rules

- Relationship type: Must be one of `Cutting`, `Offshoot`, `Corm`, `Division`, `Seed`, or `Unknown` -> `Choose a relationship type.`
- Parent selection for `Seed`: Exactly two distinct parent plants are required -> `Choose two parent plants for a seed relationship.`
- Parent selection for all other types: Exactly one parent plant is required -> `Choose a parent plant.`
- Parent plant: A plant cannot be its own parent -> `A plant cannot be its own parent.`
- Parent plants for `Seed`: The same plant cannot be selected twice -> `Choose two different parent plants.`
- Relationship date: Required -> `Choose a relationship date.`
- Relationship date: Date must be today or in the past -> `Relationship date cannot be in the future.`
- Notes: Optional plain text only. No images or attachments are allowed.

### Persistence

- Relationship records should survive app restart once local persistence exists.
- Relationship records should survive device reboot once local persistence exists.
- Parent search temporary selections are only in memory until the user confirms selection.
- Unsaved relationship form edits are only in memory and can be discarded by leaving the form according to the app's existing unsaved-change behavior.

## Integration Points

_How this feature connects to existing code._

- **Database:** Introduce a local plant relationship record when persistence exists. Each row links exactly one child plant to exactly one parent plant. A likely shape is `id`, `childPlantId`, `parentPlantId`, `relationshipType`, `relationshipDate`, `notes`, `createdAt`, and `updatedAt`. A `Seed` relationship creates two rows with the same child plant, type, date, and notes. The repository should enforce parent selection count rules so UI and data stay consistent.
- **Navigation:** Add or reuse a route for parent plant search. Plant detail rows should navigate to existing plant detail routes for related plants.
- **Shared code:** Add shared model types for relationship records and relationship type. Existing `PlantId` from `Plant.kt` should be reused so relationships stay tied to user plant records, not catalog `KnownPlant` records.
- **Platform-specific:** No Android-only or iOS-only behavior is required for version 1. Date picker implementation may vary by platform if the project already abstracts date picking.

## Acceptance Criteria

_Every item must be a testable, pass/fail statement. No ambiguity._

### AC-1: New plant can be saved without relationship

- **Given** the user is creating a new plant
- **When** they fill the required plant fields and do not tap `Add relationship`
- **Then** the plant saves successfully with no relationship record

### AC-2: Relationship section is available during plant creation

- **Given** the user is creating a new plant
- **When** the plant form is displayed
- **Then** a `Plant Relationship` section is visible
- **And** an `Add relationship` action is available

### AC-3: Single-parent relationship requires one parent

- **Given** the user has opened the relationship editor
- **When** they choose `Cutting`, `Offshoot`, `Corm`, `Division`, or `Unknown`
- **Then** the parent picker label reads `Choose parent plant`
- **And** the parent search allows a maximum of one selected parent

### AC-4: Seed relationship requires two parents

- **Given** the user has opened the relationship editor
- **When** they choose `Seed`
- **Then** the parent picker label reads `Choose parent plants`
- **And** the parent search allows a maximum of two selected parents
- **And** saving is blocked until two distinct parent plants are selected

### AC-5: Parent search filters by plant name

- **Given** the parent plant search page is open
- **When** the user types a plant name into `Search plants`
- **Then** the result list only shows matching selectable plants

### AC-6: Parent search filters by scientific name

- **Given** the parent plant search page is open
- **When** the user types a scientific name into `Search plants`
- **Then** the result list only shows matching selectable plants

### AC-7: Parent selection confirmation is disabled until complete

- **Given** the parent plant search page is open for a `Seed` relationship
- **When** only one parent plant is selected
- **Then** the `Confirm selection` button is disabled

### AC-8: Parent selection returns selected plants to form

- **Given** the parent plant search page is open and the required parent count is selected
- **When** the user taps `Confirm selection`
- **Then** the app returns to the plant form
- **And** the selected parent plant names are shown in the relationship editor

### AC-9: Back from parent search discards unconfirmed selection

- **Given** the parent plant search page is open
- **When** the user selects a plant and taps the back action without confirming
- **Then** the app returns to the plant form
- **And** the unconfirmed selection is not applied

### AC-10: Existing plant can receive a relationship later

- **Given** an existing plant has no parent relationship
- **When** the user edits the plant, taps `Add relationship`, completes required relationship fields, and saves
- **Then** the relationship is saved for that plant

### AC-11: Existing relationship can be edited

- **Given** an existing plant has a saved relationship
- **When** the user edits the relationship type, parent plants, relationship date, or notes
- **Then** the changed values are shown in the relationship editor before save

### AC-12: Editing relationship shows change confirmation

- **Given** an existing plant has a saved relationship
- **When** the user changes relationship fields and taps `Save`
- **Then** a dialog titled `Save relationship changes?` is shown
- **And** each changed field is listed with old and new values

### AC-13: Canceling edit confirmation keeps unsaved edits visible

- **Given** the `Save relationship changes?` dialog is open
- **When** the user taps `Cancel`
- **Then** the dialog closes
- **And** the user remains on the plant form with their unsaved relationship edits still visible

### AC-14: Confirming edit confirmation saves changes

- **Given** the `Save relationship changes?` dialog is open
- **When** the user taps `Save changes`
- **Then** the relationship changes are saved
- **And** reopening the plant shows the updated relationship values

### AC-15: No edit confirmation appears when relationship is unchanged

- **Given** an existing plant has a saved relationship
- **When** the user opens edit mode and taps `Save` without changing relationship fields
- **Then** no `Save relationship changes?` dialog is shown for the relationship

### AC-16: Relationship can be removed without deleting plants

- **Given** an existing plant has a saved relationship
- **When** the user taps `Remove relationship` and confirms `Remove`
- **Then** the relationship is removed from the current plant
- **And** the previously related parent plants still exist in the collection

### AC-17: Plant detail shows empty lineage state

- **Given** a plant has no parent relationships and no child relationships
- **When** the user opens the plant detail page
- **Then** the `Lineage` section shows `No lineage recorded yet.`

### AC-18: Plant detail shows immediate parents

- **Given** a plant has one or two parent plants saved through a relationship
- **When** the user opens that plant's detail page
- **Then** the `Lineage` section shows those immediate parent plants
- **And** each parent row shows plant thumbnail, plant name, relationship type, and relationship date

### AC-19: Plant detail shows immediate children

- **Given** another plant has the current plant saved as a parent
- **When** the user opens the current plant's detail page
- **Then** the `Lineage` section shows that child plant
- **And** the child row shows plant thumbnail, plant name, relationship type, and relationship date

### AC-20: Timeline does not show deeper relatives

- **Given** plant A is parent of plant B and plant B is parent of plant C
- **When** the user opens plant A's detail page
- **Then** the `Lineage` section shows plant B as a child
- **And** plant C is not shown on plant A's detail page

### AC-21: Timeline row navigates to related plant

- **Given** the `Lineage` section shows a related plant row
- **When** the user taps that row
- **Then** the app navigates to that related plant's detail page

### AC-22: Timeline is read-only

- **Given** the user is viewing the `Lineage` section on a plant detail page
- **When** the section is displayed
- **Then** no edit or remove relationship action is shown in the timeline

### AC-23: Plant cannot be its own parent

- **Given** the user is editing an existing plant's relationship
- **When** they open parent search
- **Then** the current plant cannot be selected as a parent
- **And** the disabled row explains `This plant cannot be its own parent`

### AC-24: Relationship date is required

- **Given** the user has an active relationship editor
- **When** no relationship date is set and the user tries to save
- **Then** saving is blocked
- **And** `Choose a relationship date.` is shown

### AC-25: Relationship notes are optional plain text

- **Given** the user has an active relationship editor
- **When** they leave notes empty and complete all required fields
- **Then** the relationship can be saved
- **And** no attachment or image input is shown for relationship notes

### AC-26: Deleting a plant warns about relationship deletion

- **Given** a plant has at least one parent or child relationship
- **When** the user starts permanent plant deletion
- **Then** the confirmation states `This plant has lineage relationships. Those relationship links will be deleted, but the related plants will remain.`

### AC-27: Deleting a plant removes only relationship links

- **Given** a plant has at least one parent or child relationship
- **When** the user confirms permanent deletion
- **Then** the deleted plant is removed
- **And** all relationship rows involving that plant are removed
- **And** all related plant records remain in the collection

### AC-28: Parent search includes inactive or dead plants when statuses exist

- **Given** plant status functionality exists and at least one inactive or dead plant exists
- **When** the user searches for a parent plant
- **Then** inactive or dead plants are included in search results
- **And** their status is shown consistently with the app's status UI

### AC-29: Accessibility labels describe selected parents

- **Given** the relationship editor shows selected parent plants
- **When** TalkBack focuses each selected parent row
- **Then** it announces the parent position and plant name, such as `Parent 1, Monstera cutting`

### AC-30: Timeline rows expose relationship details to accessibility services

- **Given** the lineage timeline shows a related plant
- **When** TalkBack focuses the related plant row
- **Then** it announces plant name, relationship direction, relationship type, and relationship date

### AC-31: Changing relationship type clears selected parents

- **Given** the relationship editor has one or more selected parent plants
- **When** the user changes the relationship type
- **Then** all selected parent plants are cleared
- **And** the editor shows `Choose parent plants again for this relationship type.`

### AC-32: Seed relationship creates one row per parent

- **Given** the user creates a `Seed` relationship with two selected parent plants
- **When** the plant and relationship are saved
- **Then** two relationship records are stored
- **And** both records have the same child plant, relationship type, relationship date, and notes
- **And** each record has a different parent plant

### AC-33: Future relationship date is blocked

- **Given** the user has an active relationship editor
- **When** they choose a relationship date after today's date and try to save
- **Then** saving is blocked
- **And** `Relationship date cannot be in the future.` is shown

### AC-34: Relationship section appears at bottom of form

- **Given** the user opens the plant create or edit form
- **When** the form is displayed
- **Then** the `Plant Relationship` section appears below the other plant fields and optional sections

## Implementation Considerations

_Learning-focused guidance. These are suggestions, not mandates._

### Architecture

- Relationship rules belong below the UI, ideally in a small domain/model layer or repository-level validator, because parent count rules must stay true even if another screen later creates relationships.
- This is partly an addition to the existing plant form and partly an addition to the plant detail screen. The parent search page is a new screen because inline selection will become awkward once the collection grows.
- A new relationship model is useful even if the current app state is in memory. It teaches the difference between a plant entity and a join-like entity that connects plants to other plants.
- The timeline should be derived from saved relationship records rather than stored separately. Store the facts once, then query or compute parent and child rows for display.

### Compose Multiplatform

- `LazyColumn` is a good fit for parent search results and the lineage timeline because both can grow as the collection grows.
- A `Scaffold` with a bottom bar works well for the parent search page because `Confirm selection` should remain reachable while results scroll.
- State hoisting matters here: the plant form should own the relationship draft, while the parent search screen owns temporary selection until confirmation.
- `AlertDialog` is appropriate for the edit confirmation and remove confirmation flows.
- `LaunchedEffect` may be useful for loading existing relationship data when opening an edit screen, but avoid using it for ordinary form field updates.
- If the app uses shared date pickers, reuse them. If date picker support differs by platform, keep the date selection API behind a small shared abstraction.

### Kotlin Patterns

- Model relationship type as an `enum class` or sealed type with a derived `requiredParentCount` property. This keeps UI labels and validation consistent.
- Model the saved relationship as a data class using immutable `val` properties. Use `copy()` when editing drafts.
- Consider a separate draft data class for form editing so incomplete user input does not have to pretend to be a valid saved relationship.
- Use an exhaustive `when` expression for relationship type behavior so adding a future type forces the compiler to point out missing parent-count rules.
- Use small value objects or typealiases for IDs, reusing `PlantId` so relationships cannot accidentally point at catalog `KnownPlant` IDs.

### Testing Strategy

- Unit test relationship validation rules, especially required parent counts and self-parent prevention.
- Unit test timeline derivation so parent and child rows are correct and deeper relatives are excluded.
- ViewModel tests should cover create, add later, edit with diff confirmation, remove, and delete-cascade behavior.
- Compose UI tests should cover the happy path of creating a new plant with a cutting relationship and navigating through the detail timeline.
- Add preview states for no relationship, one-parent relationship, two-parent seed relationship, empty timeline, and populated timeline.

### Learning Opportunities

- Relationship modeling is a good introduction to graph-like data without needing to build a full graph UI.
- The parent search screen is a practical example of state hoisting across navigation: temporary selection is local until confirmed.
- The edit confirmation dialog is a useful exercise in deriving a human-readable diff from two immutable data objects.
- When persistence is introduced, this feature will be a good SQLDelight migration exercise because it needs foreign keys from a relationship table back to the plant table.

## Open Questions

- No open questions for the plant relationships feature after the current review pass.
- Exact plant status labels are intentionally deferred to the future plant status feature. Expected labels are simple values such as `Active`, `Alive`, `Dead`, and `Diseased`.

_Items here should be resolved before the spec moves to "approved" status._
