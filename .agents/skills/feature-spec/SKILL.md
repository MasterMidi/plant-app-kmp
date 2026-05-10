---
name: feature-spec
description: Create structured, detailed feature specifications from scattered ideas. Use this whenever the user describes a feature, enhancement, or idea for their KMP app — even casually or offhand. Proactively offer to spec it out. Also use when the user says 'spec out', 'write a spec', 'design a feature', 'plan a feature', 'document this idea', or similar. This skill turns rough thoughts into formal specs with purpose, user flows, detailed UI/UX, acceptance criteria, and KMP implementation guidance.
---

# Feature Spec

Transform rough feature ideas into detailed, actionable specifications. This skill is
designed for a learning developer working in Kotlin Multiplatform and Compose
Multiplatform — every spec should teach as well as specify.

## Core Principles

1. **Never assume intent.** If something is ambiguous, ask. Treat the user as the domain
   expert and yourself as the interviewer whose job is to extract every detail they have
   in their head.
2. **Purpose before features.** Always start with _why_ — the problem, the value, the
   user need. Don't let the user jump to solutions before the problem is clear.
3. **Detail is non-negotiable.** Button placements, text labels, loading states, error
   states, empty states, edge cases, navigation transitions — all must be explicit.
4. **Teach through the spec.** Implementation notes should highlight KMP patterns,
   Compose APIs, architecture decisions, and Kotlin idioms worth learning.
5. **Acceptance criteria are a contract.** They must be testable, unambiguous, and
   complete enough that someone else could verify every aspect of the feature.

## When to Activate

Activate proactively when the user mentions an idea, even casually:

- "What if the app could..."
- "I think we should add..."
- "It would be cool if..."
- "We need a way to..."
- Any description of new functionality or a change to existing behavior

Also activate on direct requests: "spec this out", "write a spec", "design X", etc.

When you detect a potential feature, **do not immediately start writing a spec**.
Instead, pause and say something like: "That sounds like a feature worth speccing out.
Want me to help you write a detailed spec for it?" Let the user confirm before proceeding.

## Workflow

Follow this sequence. Do not skip steps or jump ahead.

### Phase 1: Discovery

Before touching the spec template, understand the feature. Ask questions organized
by these layers, roughly in order. Stop asking when the user's answers are clearly
complete for each layer — don't force every question if it doesn't apply.

**Layer 1 — Purpose & Value**
- What problem does this solve?
- Who benefits? (Which user persona or role?)
- How do they solve this problem today (workaround, other app, not at all)?
- What's the one-sentence value proposition?
- Why is this worth building _now_ vs. later?

**Layer 2 — Scope & Boundaries**
- What exactly is in scope for this version?
- What is explicitly out of scope? (This is just as important — prevents scope creep)
- Does this depend on other features not yet built?
- Will this change or replace any existing behavior?

**Layer 3 — User Flow**
- Walk me through the exact sequence from start to finish.
- What screen does the user start on?
- What do they tap/type/see at each step?
- How do they know they're done? What confirms success?
- Where can they go wrong? What errors are possible?

**Layer 4 — UI/UX Details (for each screen)**
- Exact position of each button, label, input field.
- What text appears in each label, button, placeholder, tooltip?
- What icons or images are needed?
- What are all the states? (loading, empty, populated, error, disabled, success)
- What animations or transitions happen between states/screens?
- How does this adapt to different screen sizes or orientations?
- What's the accessibility behavior? (content descriptions, focus order)

**Layer 5 — Data & State**
- What data does this feature need? Where does it come from?
- Is data user-entered, derived, fetched, or persisted?
- What happens to data when the app is offline? When it restarts?
- What validations are needed on user input?
- Does this touch the existing database schema? (Reference `docs/Database Design.md`)

**Layer 6 — Edge Cases**
- What if the list is empty? What if it has 500 items?
- What if the user rotates the device mid-flow?
- What if a background process interrupts (call, notification)?
- What if the user rapidly taps a button multiple times?
- What if an external dependency fails (camera, storage, etc.)?

### Phase 2: Draft

Once discovery feels complete, write the spec using the exact template below.
Save it to `docs/specs/<feature-slug>.md`. Create the `docs/specs/` directory if
it doesn't exist.

Use kebab-case for the slug (e.g., `plant-watering-reminders.md`).

Present the draft to the user and explicitly ask: "Here's the draft spec. What's
missing, wrong, or needs more detail?"

### Phase 3: Refine

Incorporate user feedback. This may loop back to Phase 1 if the user reveals new
requirements during review. Repeat until the user says the spec is complete.

Do NOT move on from the spec until the user confirms it's done. Do NOT start
implementing — the spec is the deliverable here.

---

## Spec Template

Copy this exact structure. Fill every section unless the user explicitly says
a section doesn't apply. If a section is skipped, note that the user confirmed it
is not applicable.

```markdown
# [Feature Name]

> **Status:** draft | in-review | approved | implemented
> **Created:** [date]
> **Last updated:** [date]

## Purpose & Value

_Why are we building this? One paragraph that any stakeholder could understand._

- **Problem:** [What pain point or gap exists today?]
- **Value:** [What does the user gain once this exists?]
- **Success metric:** [How will we know this is working well? e.g., "Users complete
  their daily check-in 30% faster", "No support questions about how to add a plant"]

## Goals & Non-Goals

### Goals
- [Concrete, scoped goal]
- [Another goal]

### Non-Goals (explicitly out of scope)
- [Something intentionally left for later]
- [Another out-of-scope item]

## User Stories

- As a **[user type]**, I want **[action]** so that **[benefit]**.
- As a **[user type]**, I want **[action]** so that **[benefit]**.

## Detailed Workflow

_Step-by-step, screen-by-screen. Leave nothing implicit._

### Entry Point
[How the user reaches this feature. Which screen? Which button or navigation element?
Include exact label text and icon if applicable.]

### Flow

**Step 1: [Screen / Action Name]**
- User sees: [Describe the screen layout, every visible element]
- Preconditions: [What must be true before this step?]
- User action: [What the user does — taps, types, swipes, etc.]
- System response: [What happens as a result — navigation, animation, data change]
- Validation: [What is checked? What error messages can appear?]
- Edge cases: [What could go wrong at this step?]

**Step 2: [Screen / Action Name]**
[...repeat for each step...]

**Completion**
- What does the user see when the flow is done?
- How do they return to their previous context?
- Is there an undo or confirmation period?

### State Matrix

_For every screen in this flow, enumerate all possible states._

| Screen | Loading | Empty | Normal | Error | Disabled |
|--------|---------|-------|--------|-------|----------|
| [Screen name] | [What shows during loading] | [What shows when there's no data] | [Normal populated state] | [Error state UI and message] | [When/how elements are disabled] |

## UI/UX Specifications

### Screen: [Screen Name]

**Layout description:**
[Describe layout in words — top bar, content area, bottom bar, FABs, etc.]

**Elements (top to bottom, left to right):**

| Element | Type | Position | Label/Text | Behavior | States |
|---------|------|----------|------------|----------|--------|
| [Name] | Button/TextField/Icon/etc. | [e.g., TopAppBar, trailing icon] | [Exact text or content description] | [What happens on interaction] | [Enabled, disabled, loading, error] |

**Navigation:**
- From: [where the user comes from]
- To: [where each action leads]
- Transition: [animation type, if any]

**Accessibility:**
- Content descriptions for non-text elements
- Focus order for keyboard/talkback navigation
- Any minimum touch target sizes

## Data Model & State

### Data Sources
- **User input:** [What the user types or selects]
- **Local database:** [What tables/columns are read or written]
- **Derived/computed:** [What is calculated from other data]

### State Ownership
_Where does each piece of state live?_
- [State piece]: [ViewModel / Screen / Repository / etc.]

### Validation Rules
- [Field]: [Rule — required, min/max length, format, etc.] → [Error message if invalid]

### Persistence
- What survives app restart?
- What survives a device reboot?
- Is anything only in memory?

## Integration Points

_How this feature connects to existing code._

- **Database:** [New tables? New columns? New queries? Migrations needed?]
- **Navigation:** [New routes? Changes to existing nav graph?]
- **Shared code:** [New or modified repositories, use cases, models]
- **Platform-specific:** [Anything Android-only or iOS-only?]

## Acceptance Criteria

_Every item must be a testable, pass/fail statement. No ambiguity._

### AC-[N]: [Short name]
- **Given** [precondition / starting state]
- **When** [action the user takes]
- **Then** [expected observable result]

[Number them AC-1, AC-2, etc. Group by area if there are many.]

Groups to consider:
- **Navigation** — reaching the feature, returning from it, back handling
- **Happy path** — the primary successful flow
- **Input validation** — every validation rule, every error message
- **Edge cases** — empty states, large lists, rapid taps, rotation, interruptions
- **Error handling** — what happens when things fail (network, storage, permissions)
- **Accessibility** — talkback reads correct content, touch targets are adequate
- **Persistence** — data survives app restart, correct data is saved/loaded

## Implementation Considerations

_Learning-focused guidance. These are suggestions, not mandates._

### Architecture
- Which layer does the main logic belong in? (UI → ViewModel → Repository → DataSource)
- Is this a new screen or an addition to an existing one? If existing, mention the file.
- Does this need a new ViewModel or can it reuse/extend an existing one?

### Compose Multiplatform
- Key Compose APIs likely needed (e.g., `LazyColumn`, `ModalBottomSheet`,
  `rememberLauncherForActivityResult`, `AnimatedVisibility`)
- State hoisting decisions: what state lifts to the ViewModel vs. stays in composables
- Prefer `remember` / `mutableStateOf` for local UI state,
  `StateFlow` / `collectAsState` for ViewModel state
- Side-effect APIs: `LaunchedEffect` for one-shot work, `SideEffect` for
  non-suspending work, `DisposableEffect` for cleanup

### Kotlin Patterns
- Sealed classes/interfaces for representing UI state (Loading, Success, Error, etc.)
- Data classes for model objects (prefer `val` over `var`, use `copy()` for mutations)
- Extension functions for composable previews and test helpers
- `when` expressions should be exhaustive — use sealed types to get compiler checks

### Testing Strategy
- Unit tests for ViewModels, repositories, and validation logic
- Compose UI tests for critical flows (not every screen, but the happy path)
- Preview composables for visual iteration during development

### Learning Opportunities
- If this feature involves something new to you (e.g., a `Flow` transformation,
  a custom `Modifier`, a SQLDelight migration), call it out here with a one-line
  explanation of why it's a good learning exercise.

## Open Questions

- [Question that still needs an answer before implementation]
- [Another unresolved decision]

_Items here should be resolved before the spec moves to "approved" status._
```

---

## Crafting Good Acceptance Criteria

Acceptance criteria are the most important part of the spec for a learning developer.
They are your checklist for "is this feature actually done?"

Rules for good ACs:

- **Testable:** Anyone should be able to read an AC, perform the steps, and say
  "yes, this passes" or "no, this fails" with no interpretation needed.
- **Atomic:** One behavior per AC. Don't combine "the button is blue AND navigates
  to settings AND shows a snackbar" — split into three.
- **Complete:** Cover happy path, every error path, empty states, edge cases,
  accessibility, and persistence.
- **Gherkin-style:** Use Given/When/Then format. It forces clarity about
  preconditions and expected outcomes.

**Example — Good AC:**
> **AC-3: Empty plant list shows guidance**
> - **Given** the user has no plants in their collection
> - **When** they navigate to the Plant List screen
> - **Then** a centered illustration and text "Add your first plant" is displayed
> - **And** the "Add Plant" FAB is visible in the bottom-right corner
> - **And** the search bar is hidden

**Example — Bad AC:**
> **AC-3: Empty state works**
> (Too vague. What does "works" mean? What exactly is shown? What's hidden?)

## Implementation Considerations Guidelines

The implementation notes serve your learning. Don't write implementation notes that
prescribe exact code — instead, point to the patterns, APIs, and concepts that apply,
so you can research and implement them yourself.

Good implementation note:
> "Model the UI state as a sealed interface with Loading, Success(content), and
> Error(message) variants. The ViewModel exposes a `StateFlow<PlantListUiState>` that
> the composable collects. This is a standard pattern in Compose — once you've done
> it here, you'll recognize it everywhere."

Bad implementation note:
> "Create a file `PlantListUiState.kt` with the following code: ..."

The first teaches. The second does the work for you.

## After the Spec

When the user confirms the spec is complete:

1. Remind them the spec lives at `docs/specs/<slug>.md`
2. Suggest that they update the spec as they implement — marking things done,
   adding discovered edge cases, noting what was harder or easier than expected
3. Offer: "When you're ready to implement, I can help with the code. Just point me
   at this spec."

Do NOT offer to implement in the same session unless the user explicitly asks.
The spec is the deliverable.
