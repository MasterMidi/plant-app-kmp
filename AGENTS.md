# Coding Agent Instructions

## Skills

- This repo uses Jujutsu. Load the `jujutsu` skill before any VCS operation, and do not use raw git commands.
- For Compose or Compose Multiplatform architecture/UI work, use `compose-skill` when requested and keep detailed guidance in the skill.
- For Nix flakes, dev shells, or Nix-based builds, use the `nix` skill.

## Workflow

- Keep jj changes small, atomic, and buildable.
- Describe the current jj change before editing: `jj desc -m "Message"`.
- Build before moving to the next jj change.
- Prefer verification through the dev shell, for example: `nix develop -c ./gradlew :composeApp:compileKotlinMetadata :androidApp:assembleDebug`.

## Project Shape

- `composeApp` contains shared Compose Multiplatform code and the iOS framework entry point.
- `androidApp` contains the Android application entry point and depends on `composeApp`.
- Prefer standalone classes, enums, typealiases, and objects in files named after the primary type unless they are tightly scoped helpers.
