# Plant Tasks

> **Status:** approved
> **Created:** 2026-05-10
> **Last updated:** 2026-05-10

## Purpose & Value

Plant owners often know a plant needs attention before they have time to do the work. Some tasks are recurring routines, such as washing leaves to remove dust and pests, while others are one-off reminders, such as repotting a plant when a better pot is available. This feature adds plant-specific tasks so users can plan, review, complete, skip, or decline future care work while keeping completed outcomes in the plant's history.

- **Problem:** The app can record some past care activity, but it does not give users a way to plan upcoming or unscheduled care work for individual plants.
- **Value:** Users can maintain a practical task backlog and recurring plant-care routines without relying on memory or external todo apps.
- **Success metric:** Users can create, edit, delete, complete, skip, and decline one-off or recurring tasks from both a plant detail context and a global task list, and every resolved task is saved to that plant's history.

## Goals & Non-Goals

### Goals

- Add plant-specific tasks with full create, read, update, and delete support.
- Support built-in typed task forms for `Washing / showering`, `Pruning`, `Repotting`, and `Custom`.
- Support one-off tasks with no due date, one-off tasks with a due date, and recurring tasks.
- Show tasks on the individual plant they belong to and in a global task list across all plants.
- Allow users to resolve a task as `Completed`, `Skipped`, or `Declined`.
- Save every completed, skipped, or declined task outcome to the plant history timeline.
- For recurring tasks, create the next pending task after completion, skip, or decline according to the recurrence rule.
- Preserve enough task data in history that users can understand what was intended and what happened later.

### Non-Goals (explicitly out of scope)

- Automatically creating tasks from pest treatments.
- Implementing pest treatment plans, treatment schedules, or treatment outcome tracking.
- Notifications, alarms, widgets, or OS-level reminders for task due dates.
- Calendar integrations or exporting tasks to external todo apps.
- Multi-plant bulk task creation or bulk task resolution.
- Assigning tasks to other people.
- Photos, attachments, or media on tasks.
- ML-generated task suggestions.
- Complex recurrence rules such as `every second Tuesday`, seasonal schedules, or recurrence exceptions beyond skip/decline outcomes.

## User Stories

- As a **plant owner**, I want **to create a recurring washing task for a plant** so that **dust does not build up on the leaves and pests can be mechanically removed from time to time**.
- As a **plant owner**, I want **to create a recurring pruning task for a plant** so that **I can keep it tidy on a schedule**.
- As a **plant owner**, I want **to create an unscheduled repotting task** so that **I remember the plant may need repotting even if I do not know when I will do it**.
- As a **plant owner**, I want **to see all pending tasks across plants** so that **I can decide what to work on today without opening every plant individually**.
- As a **plant owner**, I want **to complete, skip, or decline a task** so that **the app records what happened instead of only deleting the reminder**.
- As a **plant owner**, I want **resolved tasks to appear in plant history** so that **I can understand past care decisions later**.

## Detailed Workflow

_Step-by-step, screen-by-screen. Leave nothing implicit._

### Entry Point

The feature has two primary entry points:

- From a plant detail page, the user sees a `Tasks` section for that plant and an `Add task` action.
- From a top app bar action labeled `Tasks`, the user opens a global `Tasks` screen that lists pending tasks across all plants and has an `Add task` action.

When a task is created from a plant detail page, the current plant is preselected and cannot be accidentally saved without a plant. When a task is created from the global task list, the user must choose which plant the task belongs to before saving.

### Flow

**Step 1: Review Tasks On Plant Detail**

- User sees: The plant detail page with a `Tasks` section below the primary plant summary and above or near the plant history timeline.
- Preconditions: The user is viewing an existing saved plant.
- User action: The user reviews pending tasks or taps `Add task`.
- System response: Pending tasks for that plant are shown with task type, due status, recurrence label, and quick resolve actions.
- Validation: No validation runs while browsing.
- Edge cases: If the plant has no pending tasks, show `No pending tasks for this plant.` and keep `Add task` visible.

**Step 2: Review Global Task List**

- User sees: A global `Tasks` screen with grouped pending tasks across all plants.
- Preconditions: The app has at least one saved plant to create tasks for.
- User action: The user taps the `Tasks` action in the top app bar.
- System response: The app lists pending tasks sorted by urgency: overdue tasks first, due today next, scheduled future tasks next, and unscheduled tasks last.
- Validation: No validation runs while browsing.
- Edge cases: If there are no pending tasks, show `No pending tasks.` If there are no plants, show `Add a plant before creating tasks.` and hide or disable `Add task`.

**Step 3: Start Creating A Task**

- User sees: A task editor screen or sheet titled `Add task`.
- Preconditions: The user tapped `Add task` from plant detail or the global task list.
- User action: The user chooses a plant if needed, then chooses a task type.
- System response: The editor displays the typed form for the selected task type.
- Validation: A plant and task type are required before saving.
- Edge cases: If the task was started from a plant detail page and the plant is deleted before save, close the editor and show `Plant no longer exists.`

**Step 4: Fill Washing / Showering Task Form**

- User sees: The task editor with task type `Washing / showering` selected.
- Preconditions: A plant has been selected.
- User action: The user chooses a cleaning method, reason, optional notes, and schedule.
- System response: The task draft updates as fields change.
- Validation: Cleaning method and schedule type are required.
- Edge cases: If the task is recurring, the recurrence interval must be valid before saving.

**Step 5: Fill Pruning Task Form**

- User sees: The task editor with task type `Pruning` selected.
- Preconditions: A plant has been selected.
- User action: The user chooses a pruning goal, enters optional target notes, and chooses a schedule.
- System response: The task draft updates as fields change.
- Validation: Pruning goal and schedule type are required.
- Edge cases: Notes do not have a maximum length. The editor shows a live character count for context, but the count does not block saving.

**Step 6: Fill Repotting Task Form**

- User sees: The task editor with task type `Repotting` selected.
- Preconditions: A plant has been selected.
- User action: The user chooses a repotting reason, optionally enters target pot size and soil mix notes, and chooses a schedule.
- System response: The task draft updates as fields change.
- Validation: Repotting reason and schedule type are required.
- Edge cases: Repotting tasks may be unscheduled, because a plant can need repotting even if the user does not yet know when they will do it.

**Step 7: Fill Custom Task Form**

- User sees: The task editor with task type `Custom` selected.
- Preconditions: A plant has been selected.
- User action: The user enters a title, optional notes, and schedule.
- System response: The task draft updates as fields change.
- Validation: Custom task title and schedule type are required.
- Edge cases: If the title is blank after trimming whitespace, show `Task title is required.`

**Step 8: Choose Schedule**

- User sees: A `Schedule` section in the task editor with options `No date`, `Due date`, and `Repeats`.
- Preconditions: The user is creating or editing a task.
- User action: The user chooses a schedule option and fills any required schedule fields.
- System response: The visible schedule fields update immediately.
- Validation: `Due date` requires a date. `Repeats` requires a start date, interval number, and interval unit. Dates are day-only; tasks do not support due times in version 1.
- Edge cases: Past due dates are allowed so users can enter tasks that are already overdue. Recurrence interval must be at least `1`.

**Step 9: Save New Task**

- User sees: The task editor with a `Save` action.
- Preconditions: Required plant, type-specific fields, and schedule fields are valid.
- User action: The user taps `Save`.
- System response: The app creates a pending task, returns to the previous screen, and shows the task in the correct list position.
- Validation: All required fields are checked before save.
- Edge cases: If save fails, keep the editor open and show `Task could not be saved. Try again.` without losing entered values.

**Step 10: Edit Existing Task**

- User sees: A task detail/editor screen titled `Edit task`, prefilled with the task's current plant, type, type-specific fields, and schedule.
- Preconditions: The selected task is still pending.
- User action: The user changes fields and taps `Save`.
- System response: The app updates the pending task and returns to the previous context.
- Validation: Same validation rules as create.
- Edge cases: If the task was resolved or deleted elsewhere before save, show `Task is no longer pending.` and return to the latest task list.

**Step 11: Delete Pending Task**

- User sees: A `Delete task` action on the edit task screen or task overflow menu.
- Preconditions: The selected task is pending.
- User action: The user taps `Delete task`.
- System response: The app shows a confirmation dialog.
- Validation: Not applicable before confirmation.
- Edge cases: Deleting a pending task does not create a plant history entry, because no care decision or outcome happened.

**Step 12: Confirm Delete**

- User sees: A confirmation dialog with title `Delete task?`, body `This pending task will be permanently removed. No plant history entry will be created.`, confirm action `Delete`, and cancel action `Cancel`.
- Preconditions: A delete confirmation dialog is open.
- User action: The user taps `Delete`.
- System response: The app removes the pending task and returns to the latest task list or plant detail page.
- Validation: Not applicable.
- Edge cases: If the user taps `Cancel` or dismisses the dialog, no task data changes.

**Step 13: Complete Task**

- User sees: A pending task row or task detail page with a `Complete` action.
- Preconditions: The selected task is pending.
- User action: The user taps `Complete`, optionally adds completion notes, and confirms.
- System response: The app records the current time as the completion time, writes a plant history entry, and removes the completed task from pending tasks.
- Validation: Completion notes are optional.
- Edge cases: For a recurring task, the app creates the next pending task according to the recurrence rule after saving the completed outcome.

**Step 14: Skip Task**

- User sees: A pending task row or task detail page with a `Skip` action.
- Preconditions: The selected task is pending.
- User action: The user taps `Skip`, optionally adds a reason, and confirms.
- System response: The app records the current time as the skip time, writes a plant history entry, and removes the skipped task from pending tasks.
- Validation: Skip reason is optional.
- Edge cases: For a recurring task, the app creates the next pending task according to the recurrence rule. For a one-off task, the task becomes resolved and no replacement is created.

**Step 15: Decline Task**

- User sees: A pending task row or task detail page with a `Decline` action.
- Preconditions: The selected task is pending.
- User action: The user taps `Decline`, optionally adds a reason, and confirms.
- System response: The app records the current time as the decline time, writes a plant history entry, and removes the declined task from pending tasks.
- Validation: Decline reason is optional.
- Edge cases: Decline applies only to the current pending task. For a recurring task, the app creates the next pending task according to the recurrence rule. If the user wants to stop the recurring task entirely, they delete the pending recurring task instead.

**Step 16: View Resolved Task In Plant History**

- User sees: The plant history timeline with an entry for the resolved task.
- Preconditions: At least one task has been completed, skipped, or declined for the plant.
- User action: The user opens the plant's history timeline.
- System response: The history entry shows task type, outcome, resolved timestamp, relevant type-specific details, and optional notes/reason.
- Validation: Not applicable.
- Edge cases: If the original pending task was later deleted from task tables, the history entry remains because it is the permanent care record.

**Completion**

- The user is done when the task appears as pending in the plant/global task lists or when the resolved outcome appears in plant history.
- The user returns to their previous context using existing back navigation or save behavior.
- Deleting a pending task requires confirmation and has no undo period.
- Completing, skipping, or declining a task creates history immediately and has no undo in version 1.

### State Matrix

_For every screen in this flow, enumerate all possible states._

| Screen | Loading | Empty | Normal | Error | Disabled |
|--------|---------|-------|--------|-------|----------|
| Plant detail tasks section | Use the plant detail screen's existing loading state if present | Show `No pending tasks for this plant.` and `Add task` | Show pending tasks for the plant with type, due status, recurrence label, and actions | Show `Tasks could not be loaded.` while keeping the rest of plant detail usable | Resolve/edit actions disabled while a task mutation is saving |
| Global Tasks screen | Show progress indicator if tasks load asynchronously | Show `No pending tasks.`; if no plants exist, show `Add a plant before creating tasks.` | Show grouped pending tasks across plants sorted by urgency | Show `Tasks could not be loaded. Try again.` with retry action | `Add task` disabled when there are no plants |
| Add/Edit task editor | Not expected for current in-memory state; show progress if loading existing task asynchronously later | Not applicable | Show plant selector, task type selector, typed fields, schedule section, `Save`, and `Cancel` | Show field errors inline and save errors near the save action | `Save` disabled or blocked while required fields are invalid or save is in progress |
| Delete confirmation dialog | Not expected | Not applicable | Show title, consequence text, `Cancel`, and `Delete` | If deletion fails, keep task visible and show app's normal error pattern | `Delete` disabled while deletion is in progress if async persistence is introduced |
| Resolve task dialog/sheet | Not expected | Not applicable | Show selected outcome, optional notes/reason field, `Cancel`, and confirm action | If resolution fails, keep task pending and show `Task could not be updated. Try again.` | Confirm action disabled while resolution is saving |
| Plant history timeline | Use existing timeline loading state if present | Existing empty history state | Show resolved task entries among other care events chronologically | Show existing timeline error state | History entries are read-only |

## UI/UX Specifications

### Screen: Plant Detail Tasks Section

**Layout description:**

Add a `Tasks` section to the plant detail page. It should be compact enough to scan quickly, because the plant detail page also needs room for plant identity, care summaries, and history. Show only pending tasks in this section; resolved outcomes belong in plant history.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Section heading | Text | Plant detail content | `Tasks` | Identifies pending work for this plant | Always visible for existing plants |
| Add task action | Button or text button | Near section heading | `Add task` | Opens task editor with current plant preselected | Enabled normally |
| Empty task message | Text | Section body | `No pending tasks for this plant.` | Explains empty state | Visible only when plant has no pending tasks |
| Task row | Row/list item | Section body | Task title/type, due status, recurrence label | Opens task detail/editor or action sheet | Normal, overdue, due today, future, unscheduled |
| Complete action | Button/icon button | Task row quick action or overflow | `Complete` / content description `Complete task` | Opens completion dialog/sheet | Enabled for pending tasks |
| More actions | Icon button | Trailing side of task row | Content description `Task actions` | Opens actions for edit, skip, decline, delete | Enabled for pending tasks |

**Navigation:**

- From: Existing plant detail page.
- To: Add/Edit task editor, resolve task dialog/sheet, or task action menu.
- Transition: Use existing app navigation and Material dialog/sheet behavior.

**Accessibility:**

- Task rows should announce plant task type, due status, and recurrence status.
- `Complete task` and `Task actions` must be separate accessible actions.
- Overdue status must be communicated by text, not color alone.
- Touch targets for quick actions must meet Material minimum target size.

### Screen: Global Tasks Screen

**Layout description:**

Use a full-screen task inbox. A top app bar shows `Tasks`. The content uses a vertically scrollable list grouped by due status so urgent tasks are easy to find. Each row includes the plant name because this screen combines tasks from many plants.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Top app bar title | Text | Top app bar | `Tasks` | Identifies screen | Always visible |
| Add task action | Button/FAB/top bar action | Primary screen action | `Add task` | Opens task editor with plant selector visible | Disabled if no plants exist |
| Group heading | Text | List content | `Overdue`, `Due today`, `Upcoming`, `No date` | Groups tasks by urgency | Visible only when group has tasks |
| Task row plant name | Text | Task row | Plant display name | Shows task owner | Always visible in global list |
| Task row summary | Text | Task row | Task title/type and due label | Opens task detail/editor or action sheet | Normal, overdue, due today, future, unscheduled |
| Empty state title | Text | Content center | `No pending tasks.` | Explains there is no work | Visible when plants exist but no tasks exist |
| No plants message | Text | Content center | `Add a plant before creating tasks.` | Explains why tasks cannot be created | Visible when there are no plants |

**Navigation:**

- From: Top app bar `Tasks` action.
- To: Add/Edit task editor, resolve task dialog/sheet, or selected plant detail page if the row supports plant navigation.
- Transition: Use the app's existing full-screen navigation transition.

**Accessibility:**

- Group headings should be exposed as headings where the Compose semantics support it.
- Each row should read as `[task], [plant], [due status], [recurrence]`.
- Empty states should be reachable by TalkBack and not rely on images alone.

### Screen: Add/Edit Task Editor

**Layout description:**

Use a full-screen form or modal sheet consistent with existing plant forms. A full-screen form is preferred if typed fields grow beyond a few controls; a sheet is acceptable for the first implementation if the form remains compact.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Editor title | Text | Top app bar or sheet header | `Add task` or `Edit task` | Communicates mode | Add/edit |
| Plant selector | Picker/search trigger | Form body | `Plant` | Selects task plant when launched globally | Required; prefilled and locked when launched from plant detail |
| Task type selector | Dropdown/segmented selector | Form body | `Task type` | Selects typed form | Required |
| Washing method selector | Dropdown/chips | Washing form | `Cleaning method` | Options `Shower`, `Rinse`, `Wipe leaves`, `Other` | Required for washing tasks |
| Washing reason selector | Dropdown/chips | Washing form | `Reason` | Options `Dust removal`, `Pest prevention`, `Routine care`, `Other` | Optional unless product direction later makes it required |
| Pruning goal selector | Dropdown/chips | Pruning form | `Pruning goal` | Options `Remove dead growth`, `Shape plant`, `Control size`, `Propagation cuttings`, `Other` | Required for pruning tasks |
| Pruning target notes | Multiline text field | Pruning form | Placeholder `What should be pruned?` | Stores optional target notes | Optional |
| Repotting reason selector | Dropdown/chips | Repotting form | `Reason` | Options `Rootbound`, `Soil refresh`, `Larger pot`, `Pot damage`, `Other` | Required for repotting tasks |
| Target pot size field | Text field | Repotting form | `Target pot size` | Stores optional free text | Optional |
| Soil mix notes field | Multiline text field | Repotting form | Placeholder `Soil mix notes` | Stores optional free text | Optional |
| Custom title field | Text field | Custom form | `Task title` | Stores custom task title | Required for custom tasks |
| Notes field | Multiline text field | All forms | Placeholder `Add notes` | Stores optional task notes | Optional |
| Notes character count | Text | Below notes field | `[N] characters` | Shows how much the user has written | Updates live and never blocks saving |
| Schedule selector | Segmented/buttons/radio group | Form body | `Schedule` | Options `No date`, `Due date`, `Repeats` | Required |
| Due date field | Date picker trigger | Schedule section | `Due date` | Selects a day-only due date for one-off task | Required when `Due date` is selected; no time-of-day input |
| Repeat start field | Date picker trigger | Schedule section | `Starts` | Selects first day-only due date for recurring task | Required when `Repeats` is selected; no time-of-day input |
| Repeat interval field | Number input + unit picker | Schedule section | `Repeat every` | Stores interval like `4 weeks` | Required when `Repeats` is selected |
| Cancel action | Button/text button | App bar or form actions | `Cancel` | Discards unsaved editor changes | Enabled normally |
| Save action | Button | App bar or form actions | `Save` | Creates or updates pending task | Disabled or blocked while invalid/saving |

**Navigation:**

- From: Plant detail tasks section or global task list.
- To: Previous screen after save or cancel.
- Transition: Use existing full-screen form or sheet transition.

**Accessibility:**

- The selected task type should be announced when it changes.
- Fields that appear conditionally after choosing a task type or schedule should be reachable in visual order.
- Validation messages should be associated with the fields they describe.
- Date picker controls must have text labels that are readable without visual context.

### Screen: Resolve Task Dialog/Sheet

**Layout description:**

Use a dialog or bottom sheet for resolving tasks so users can make a quick decision from either plant detail or the global list. The sheet title and confirm action change based on the chosen outcome.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| Title | Text | Dialog/sheet header | `Complete task`, `Skip task`, or `Decline task` | Communicates selected outcome | One title per outcome |
| Task summary | Text | Body | Task type/title and plant name | Confirms what will be resolved | Always visible |
| Outcome timestamp | Text | Body | `Completed now`, `Skipped now`, or `Declined now` | Records the current time when the user confirms | Not editable in version 1 |
| Notes/reason field | Multiline text field | Body | Placeholder `Add notes` or `Add reason` | Stores optional outcome notes | Optional |
| Notes/reason character count | Text | Below notes/reason field | `[N] characters` | Shows how much the user has written | Updates live and never blocks saving |
| Recurrence explanation | Text | Body | `Next task will be created for [date].` | Explains consequence | Visible for recurring tasks |
| Cancel action | Button/text button | Actions | `Cancel` | Closes without changes | Enabled normally |
| Confirm action | Button | Actions | `Complete`, `Skip`, or `Decline` | Saves outcome and updates pending tasks | Disabled while saving |

**Navigation:**

- From: Pending task row, task detail/editor, or task action menu.
- To: Previous screen after confirm or cancel.
- Transition: Use standard dialog or bottom sheet behavior.

**Accessibility:**

- The dialog/sheet title must identify the destructive or resolving action.
- The recurrence consequence text should be read before the confirm action.
- `Decline` should be visually and semantically distinct from `Skip`, because it records that the user actively decided against the current task rather than postponing it.

### Screen: Plant History Timeline

**Layout description:**

Resolved task outcomes appear as read-only care history entries in the existing plant history timeline. These entries should look related to other care events but include the task outcome so users can distinguish completed work from skipped or declined work.

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| History row type | Text/icon | Timeline row | `Washing / showering`, `Pruning`, `Repotting`, or custom title | Shows what task was resolved | Always visible |
| Outcome label | Text/chip | Timeline row | `Completed`, `Skipped`, or `Declined` | Shows result | Always visible for task-derived entries |
| Resolved timestamp | Text | Timeline row | Local date/time | Shows when outcome was recorded | Always visible |
| Task details | Text | Timeline row details | Relevant typed fields and notes | Provides context | Hidden if no details exist |

**Navigation:**

- From: Plant detail page history section.
- To: No edit flow for resolved task history in version 1.
- Transition: Not applicable.

**Accessibility:**

- History rows should read outcome, task type, and timestamp in one coherent sentence.
- Outcome labels must not rely on color only.

## Data Model & State

### Data Sources

- **User input:** Plant selection, task type, type-specific fields, notes, schedule, resolution outcome, and optional resolution notes/reasons.
- **Local database:** A future persistent implementation needs task storage and history storage. Current app state may initially be in-memory if persistence has not landed yet.
- **Derived/computed:** Due status (`overdue`, `due today`, `upcoming`, `no date`), recurrence label, next task date, sorted global task groups, and plant-specific task lists.

### Suggested Domain Model

Use `Task` for pending or scheduled work. The user-facing term and code-facing term should both be `Task` so the feature stays broad enough for future task sources, such as pest treatments. Historical outcomes may still be written through the app's existing or planned plant history mechanism.

```kotlin
Task
  id              : TaskId
  plantId         : PlantId
  type            : TaskType
  status          : TaskStatus   // Pending only in normal task lists
  schedule        : TaskSchedule
  createdAt       : Instant
  updatedAt       : Instant
  typeData        : typed fields, columns, or serialized payload
  notes           : String?

TaskType
  WashingShowering
  Pruning
  Repotting
  Custom

TaskSchedule
  Unscheduled
  DueDate(date: LocalDate)
  Recurring(startDate: LocalDate, interval: Int, unit: Day | Week | Month)

TaskOutcome
  Completed
  Skipped
  Declined
```

For history, a resolved task should create a task-derived plant history row containing at minimum:

- `id`
- `plantId`
- `sourceTaskId`
- `taskType`
- `outcome`
- `resolvedAt` as the current time when the user confirms completion, skip, or decline
- `scheduledFor`
- `notes` or `reason`
- type-specific snapshot data

Recurring tasks are stored as regular pending tasks with recurrence data. When the user completes, skips, or declines a recurring task, the current task is resolved into plant history and a new future `Task` is created from the same recurrence rule. There is no separate parent series object in version 1.

### State Ownership

_Where does each piece of state live?_

- Pending task list: ViewModel/app state, filtered by plant for plant detail and grouped globally for the global task list.
- Task editor draft: Screen/ViewModel state while creating or editing; persisted only when `Save` succeeds.
- Resolve task dialog state: Local composable state or hoisted screen state until confirmed.
- Derived due groups: ViewModel-level derived state so sorting is consistent between screens.
- Plant history entries: Same owner as other plant history/care events.

### Validation Rules

- Plant: Required -> `Choose a plant.`
- Task type: Required -> `Choose a task type.`
- Custom task title: Required after trimming whitespace -> `Task title is required.`
- Washing cleaning method: Required -> `Choose a cleaning method.`
- Pruning goal: Required -> `Choose a pruning goal.`
- Repotting reason: Required -> `Choose a repotting reason.`
- Schedule type: Required -> `Choose a schedule.`
- Due date: Required when `Due date` is selected; date-only with no time-of-day -> `Choose a due date.`
- Repeat start date: Required when `Repeats` is selected; date-only with no time-of-day -> `Choose a start date.`
- Repeat interval number: Required and must be at least `1` -> `Repeat interval must be at least 1.`
- Repeat unit: Required when `Repeats` is selected -> `Choose a repeat unit.`
- Notes/reason fields: Optional plain text with no maximum length. Editors show a live character count for context, but character count never blocks saving.
- Task ownership: Edited, deleted, or resolved task must belong to an existing plant -> reject or ignore invalid references.

### Persistence

- Pending tasks must survive app restart once the app has persistent storage.
- Resolved task history entries must survive app restart and device reboot once the app has persistent storage.
- The history snapshot should remain readable even if the original pending task is later deleted.
- In the current in-memory app, task behavior may follow the existing restart behavior until the persistence layer is implemented.

## Integration Points

_How this feature connects to existing code._

- **Database:** Add task tables when SQLDelight/local persistence exists. Suggested storage separates `Task` pending work from plant history entries. If the MVP `CareEvent` table already exists by implementation time, resolved task outcomes should insert compatible history events with task outcome metadata.
- **Navigation:** Add a global `Tasks` route opened from a top app bar `Tasks` action for now. Add task entry points from plant detail and the global task list. Broader navigation UX is intentionally left for later.
- **Shared code:** Add shared models for task type, schedule, status, outcome, and typed form data. Add ViewModel/repository operations for create, update, delete, complete, skip, decline, list by plant, and list global pending tasks.
- **Plant history:** Resolved tasks write read-only history entries using the same timeline mechanism planned for care events.
- **Future pest treatments:** Pest treatment plans can later create `Task` records automatically, but this feature only builds the flexible task infrastructure and manual task creation.
- **Platform-specific:** No Android-only or iOS-only behavior is expected. UI and task logic should live in shared Compose Multiplatform code.

## Acceptance Criteria

_Every item must be a testable, pass/fail statement. No ambiguity._

### AC-1: Plant detail shows task section

- **Given** a saved plant exists
- **When** the user opens that plant's detail page
- **Then** a `Tasks` section is visible
- **And** an `Add task` action is visible

### AC-2: Empty plant task section shows guidance

- **Given** a plant has no pending tasks
- **When** the user opens that plant's detail page
- **Then** the `Tasks` section shows `No pending tasks for this plant.`
- **And** the `Add task` action remains available

### AC-3: Global task list shows pending tasks across plants

- **Given** multiple plants have pending tasks
- **When** the user opens the global `Tasks` screen
- **Then** pending tasks from all plants are shown
- **And** each row includes the plant name

### AC-4: Global task list groups tasks by urgency

- **Given** pending tasks include overdue, due today, future, and unscheduled tasks
- **When** the user opens the global `Tasks` screen
- **Then** overdue tasks appear before due-today tasks
- **And** due-today tasks appear before future tasks
- **And** future tasks appear before unscheduled tasks

### AC-5: No-plant global empty state blocks creation

- **Given** the user has no saved plants
- **When** the user opens the global `Tasks` screen
- **Then** the screen shows `Add a plant before creating tasks.`
- **And** `Add task` is hidden or disabled

### AC-6: Creating task from plant detail preselects plant

- **Given** the user is viewing a plant detail page
- **When** the user taps `Add task`
- **Then** the task editor opens with that plant selected
- **And** the task cannot be saved for a different plant unless product design explicitly allows changing the preselected plant

### AC-7: Creating task globally requires plant selection

- **Given** the user opens `Add task` from the global task list
- **When** no plant is selected
- **Then** saving is blocked
- **And** the editor shows `Choose a plant.`

### AC-8: Washing task can be created

- **Given** the task editor has a plant selected
- **When** the user selects `Washing / showering`, chooses a cleaning method, chooses a valid schedule, and taps `Save`
- **Then** a pending washing/showering task is created for the selected plant
- **And** the task appears on the plant detail task section
- **And** the task appears on the global `Tasks` screen

### AC-9: Pruning task can be created

- **Given** the task editor has a plant selected
- **When** the user selects `Pruning`, chooses a pruning goal, chooses a valid schedule, and taps `Save`
- **Then** a pending pruning task is created for the selected plant

### AC-10: Repotting task can be created without a due date

- **Given** the task editor has a plant selected
- **When** the user selects `Repotting`, chooses a repotting reason, selects `No date`, and taps `Save`
- **Then** a pending unscheduled repotting task is created for the selected plant
- **And** it appears in the `No date` group on the global `Tasks` screen

### AC-11: Custom task requires title

- **Given** the task editor has `Custom` selected
- **When** the custom task title is blank and the user tries to save
- **Then** saving is blocked
- **And** the editor shows `Task title is required.`

### AC-12: Due-date schedule requires date

- **Given** the task editor has `Due date` selected in the schedule section
- **When** no due date is selected and the user tries to save
- **Then** saving is blocked
- **And** the editor shows `Choose a due date.`

### AC-13: Recurring schedule requires valid interval

- **Given** the task editor has `Repeats` selected in the schedule section
- **When** the repeat interval is empty or less than `1` and the user tries to save
- **Then** saving is blocked
- **And** the editor shows `Repeat interval must be at least 1.`

### AC-14: Pending task can be edited

- **Given** a pending task exists
- **When** the user opens `Edit task`, changes a valid field, and taps `Save`
- **Then** the pending task is updated
- **And** the updated values are visible in the plant detail task section and global task list

### AC-15: Pending task can be deleted after confirmation

- **Given** a pending task exists
- **When** the user chooses `Delete task` and confirms `Delete`
- **Then** the pending task is removed from the plant detail task section
- **And** the pending task is removed from the global task list
- **And** no plant history entry is created

### AC-16: Delete cancellation preserves task

- **Given** the delete task confirmation dialog is open
- **When** the user taps `Cancel` or dismisses the dialog
- **Then** the task remains pending
- **And** no plant history entry is created

### AC-17: Completing a one-off task creates history

- **Given** a one-off pending task exists
- **When** the user completes the task
- **Then** the task is removed from pending task lists
- **And** a plant history entry is created with outcome `Completed`
- **And** no replacement pending task is created

### AC-18: Skipping a one-off task creates history

- **Given** a one-off pending task exists
- **When** the user skips the task
- **Then** the task is removed from pending task lists
- **And** a plant history entry is created with outcome `Skipped`
- **And** no replacement pending task is created

### AC-19: Declining a one-off task creates history

- **Given** a one-off pending task exists
- **When** the user declines the task
- **Then** the task is removed from pending task lists
- **And** a plant history entry is created with outcome `Declined`
- **And** no replacement pending task is created

### AC-20: Completing a recurring task creates next task

- **Given** a recurring pending task exists with a valid recurrence rule
- **When** the user completes the task
- **Then** a plant history entry is created with outcome `Completed`
- **And** the completed task is removed from pending task lists
- **And** a new pending task is created for the next recurrence date

### AC-21: Skipping a recurring task creates next task

- **Given** a recurring pending task exists with a valid recurrence rule
- **When** the user skips the task
- **Then** a plant history entry is created with outcome `Skipped`
- **And** the skipped task is removed from pending task lists
- **And** a new pending task is created for the next recurrence date

### AC-22: Declining a recurring task creates next task

- **Given** a recurring pending task exists
- **When** the user declines the task
- **Then** a plant history entry is created with outcome `Declined`
- **And** the declined task is removed from pending task lists
- **And** a new pending task is created for the next recurrence date

### AC-23: Resolved task history preserves task details

- **Given** a task has typed fields and notes
- **When** the task is completed, skipped, or declined
- **Then** the plant history entry includes the task type
- **And** the plant history entry includes the outcome
- **And** the plant history entry includes relevant typed field values and notes/reason when present

### AC-24: Pending tasks survive restart when persistence exists

- **Given** persistent storage is implemented and a pending task exists
- **When** the app is closed and reopened
- **Then** the pending task is still visible on the plant detail task section and global task list

### AC-25: Resolved task history survives restart when persistence exists

- **Given** persistent storage is implemented and a task has been resolved
- **When** the app is closed and reopened
- **Then** the plant history entry for the resolved task is still visible

### AC-26: Rapid resolve taps do not duplicate outcomes

- **Given** a pending task is visible
- **When** the user rapidly taps the resolve confirmation action multiple times
- **Then** only one history entry is created
- **And** the task is resolved only once

### AC-27: Task actions are accessible

- **Given** TalkBack or another screen reader is active
- **When** focus moves through task rows and actions
- **Then** each row announces task title/type, plant name when shown globally, due status, and recurrence status
- **And** each action has a clear accessible label

### AC-28: Top bar opens global task list

- **Given** the app top bar is visible on the screen chosen for version 1 task access
- **When** the user taps `Tasks`
- **Then** the global `Tasks` screen opens

### AC-29: Task due dates are date-only

- **Given** the user is creating or editing a task with `Due date` selected
- **When** the user chooses a due date
- **Then** the editor asks for a calendar date only
- **And** no time-of-day field is shown

### AC-30: Notes show character count without blocking save

- **Given** a task editor or resolve task dialog has a notes or reason field
- **When** the user types text into that field
- **Then** a live character count is shown
- **And** saving is not blocked because of note length

## Implementation Considerations

_Learning-focused guidance. These are suggestions, not mandates._

### Architecture

- Keep pending tasks and historical entries conceptually separate. A pending `Task` answers “what should I do?”, while plant history answers “what happened?”.
- Put recurrence calculation outside composables, ideally in ViewModel/domain logic, so it can be unit tested without UI.
- Use repository-style operations even if the first version is in-memory. The operations should map cleanly to SQLDelight later: create, update, delete, resolve, list by plant, and list all pending.
- Treat resolving a task as one domain operation that writes history and updates/removes pending task state together.

### Compose Multiplatform

- `LazyColumn` is appropriate for both plant-specific task lists and the global grouped task list.
- Conditional typed forms can be modeled with a selected task type and a `when` expression that renders only that type's fields.
- Use dialog or bottom sheet patterns for quick task resolution. Use a full-screen form if task creation/editing becomes too dense for a sheet.
- Keep form field state hoisted to the task editor state holder so validation and save enablement are not scattered across composables.

### Kotlin Patterns

- Model task type and schedule as sealed interfaces or enums plus data classes so `when` expressions are exhaustive.
- Prefer immutable `data class` editor state and update with `copy()`.
- Use `kotlinx-datetime.LocalDate` for due/start dates and `Instant` for creation/resolution timestamps.
- Consider a typed payload model such as `TaskDetails.WashingShowering`, `TaskDetails.Pruning`, `TaskDetails.Repotting`, and `TaskDetails.Custom` rather than a loose map of optional fields.

### Testing Strategy

- Unit test validation for every typed task form and schedule option.
- Unit test recurrence next-date calculation for day, week, and month intervals.
- Unit test resolve behavior so complete/skip/decline create exactly one history entry and update pending state correctly.
- Compose UI test the happy path for creating a task from plant detail and resolving it into plant history.
- Compose UI test the global empty states for no plants and no pending tasks.

### Learning Opportunities

- This feature is a good place to practice sealed state models because task details and schedules have several valid shapes.
- Recurrence handling is a good exercise in keeping business logic out of composables.
- The history snapshot requirement is a useful example of why persisted historical events should not depend on mutable future task records.

## Open Questions

- None currently.
