# Coding Agent Instructions

Always do exploration and longer tasks with subagents, to prevent cluttering the main context.

## Skills

- This repo uses Jujutsu. Load the `jujutsu` skill before any VCS operation, and do not use raw git commands.
- For Compose or Compose Multiplatform architecture/UI work, use `compose-skill` when requested and keep detailed guidance in the skill.
- For Nix flakes, dev shells, or Nix-based builds, use the `nix` skill.
- After making changes, load and follow the `change-verification` skill to verify builds, checks, app launch, emulator handling, and crash logs before reporting completion.

## Workflow

- Keep jj changes small, atomic, and buildable.
- Describe the current jj change before editing: `jj desc -m "Message"`.
- Verify changes with the `change-verification` skill before moving to the next jj change.
- Prefer verification through the dev shell and include an Android app launch when feasible.

## Coding Style

- Keep functions small within reason.
- Each function should do one thing, not many things.
- Keep each function at one level of abstraction to avoid large functions that are hard to work with.

## Project Shape

- `composeApp` contains shared Compose Multiplatform code and the iOS framework entry point.
- `androidApp` contains the Android application entry point and depends on `composeApp`.
- Prefer standalone classes, enums, typealiases, and objects in files named after the primary type unless they are tightly scoped helpers.
