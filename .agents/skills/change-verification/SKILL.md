---
name: change-verification
description: Verify Plant App code changes end-to-end before reporting completion. Use this skill after any implementation, refactor, dependency, database, migration, UI, Compose, Android, iOS, or documentation-affecting change in this repository. This skill requires Gradle checks/builds plus launching the Android app from the command line, starting an emulator when needed, and inspecting crash logs so agents do not stop at compile-only verification.
---

# Change Verification

Use this skill to prove a change works in the real Plant App environment. The app can compile while still crashing on launch, especially after database, migration, dependency, or initialization changes, so verification must include an actual app start whenever feasible.

## Verification Goal

Show that the code builds, tests/checks pass, the Android app installs, the Android app launches, and the crash log does not show a new fatal exception from `org.michael.plantapp`.

## Standard Workflow

1. Run shared checks through the Nix dev shell:

```bash
nix develop -c ./gradlew :composeApp:check
```

2. Build shared metadata and the Android debug app:

```bash
nix develop -c ./gradlew :composeApp:compileKotlinMetadata :androidApp:assembleDebug
```

3. Check for a connected Android device or emulator:

```bash
nix develop -c adb devices -l
```

4. If no device is listed as `device`, start the emulator. Prefer the repo's configured AVD name:

```bash
nix develop -c emulator -avd plant-app-test -gpu auto
```

Then wait for boot:

```bash
nix develop -c adb wait-for-device
```

5. Install, launch, and inspect crash logs from the command line:

```bash
nix develop -c sh -lc './gradlew :androidApp:installDebug && adb logcat -c && adb shell am start -W -n org.michael.plantapp/.MainActivity && sleep 3 && adb logcat -d -b crash'
```

6. If the crash buffer contains a `FATAL EXCEPTION` for `org.michael.plantapp`, treat verification as failed. Diagnose the stack trace, fix the issue, then rerun the relevant build/check and launch steps.

## When To Run More

- For SQLDelight table, query, or migration changes, run both the build and app-launch steps. Existing installed databases can expose migration bugs that clean builds miss.
- For UI/navigation changes, launch the app and exercise the changed screen or flow manually when possible.
- For Android entry point, manifest, dependency, or driver changes, install and launch from the command line even if `assembleDebug` passed.
- For docs-only changes, `:composeApp:check` may be enough unless the docs describe commands or generated artifacts that should be validated.
- For Nix or dev-shell changes, load the `nix` skill and verify the affected `nix develop` command still works.

## Emulator Handling

If `plant-app-test` does not exist, create it with the repo command from the dev shell:

```bash
nix develop -c create-avd
```

If emulator startup hangs or hardware acceleration is unavailable, report that app-launch verification was blocked, include the exact command and failure, and still run Gradle checks/builds.

Do not wipe app data or uninstall the app unless the user explicitly asks or the verification goal specifically requires a fresh install. Preserving installed data helps catch migration problems.

## Reporting Format

In the final response, include a short verification summary:

```text
Verification:
- PASS/FAIL: nix develop -c ./gradlew :composeApp:check
- PASS/FAIL: nix develop -c ./gradlew :composeApp:compileKotlinMetadata :androidApp:assembleDebug
- PASS/FAIL/BLOCKED: Android install + command-line launch
- Crash log: no new org.michael.plantapp crash / crash found / blocked
```

If anything is blocked, say why and what command failed. If anything fails, do not present the task as complete until it has been fixed or the user chooses to defer it.
