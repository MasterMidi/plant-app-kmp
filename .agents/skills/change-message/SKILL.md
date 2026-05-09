---
name: change-message
description: Generate, rewrite, review, and validate useful VCS-agnostic change or commit messages. Use this whenever the user asks for a commit message, jj change description, git commit text, changelist message, or wants existing message text checked, even if they mention a specific VCS like Jujutsu or Git.
---

# Change Message

Use this skill to create messages for changes, commits, patches, or changelists without assuming a specific VCS. The message should make the future reader understand what changed and why it was worth recording.

The preferred style is lowercase conventional first lines with a scoped type:

```text
<type>(<scope>): <summary>

<body, when useful>

<footer, when useful>
```

The first line is required. The body and footer are optional.

## Core Principles

- Optimize for future readers who need to review, bisect, revert, or audit the change.
- Explain why the change exists, not only which files moved or which code changed.
- Keep mechanical changes short. Do not force a body for dependency bumps, lockfile refreshes, formatting, or generated output.
- Add a body for behavior changes, bug fixes, refactors with design intent, performance changes, migrations, and risky changes.
- Stay VCS-agnostic in the skill output unless the user explicitly asks for a command. The same message should work for jj, Git, Mercurial, or another change tracker.

## First Line

Use this shape:

```text
type(scope): summary
```

Allowed types:

- `feat` for new user-visible behavior or capability
- `fix` for a bug fix or incorrect behavior
- `refactor` for internal restructuring without intended behavior change
- `chore` for maintenance, dependency bumps, generated files, cleanup, or repository housekeeping
- `docs` for documentation-only changes
- `test` for test-only changes
- `build` for packaging, build systems, dependency resolution, or release artifacts
- `ci` for CI/CD configuration and automation
- `perf` for performance improvements
- `revert` for reverting an earlier change

Scope should name the affected area, package, component, source, command, or system. Prefer stable domain names over file names.

Good scopes:

- `auth`
- `deps`
- `skm`
- `retsinformation`
- `pipeline`
- `docs`

Avoid scopes like `misc`, `stuff`, `files`, or `code` unless they are real project names.

Summary rules:

- Use lowercase type names by default.
- Keep the summary concise, ideally 72 characters or less.
- Do not end the summary with a period.
- Prefer an imperative or descriptive verb phrase.
- Avoid vague summaries like `update`, `fix stuff`, `changes`, `wip`, or `misc cleanup`.

## Body

Use a body when the first line alone would not explain the change's purpose, tradeoff, or impact.

The body should answer the useful subset of:

- What changed?
- Why did it change?
- What behavior, risk, migration, or follow-up should the reader know?

Write the body as plain paragraphs. Do not restate the type/scope. Do not list every file unless the file list itself explains the change.

## Footer

Use a footer only for metadata or reviewer-critical notes:

```text
breaking-change: <impact and migration path>
refs: <issue, ticket, or URL>
migration: <operator or user action required>
follow-up: <known next step>
```

Prefer lowercase footer keys to match the lowercase conventional style.

## Message Selection

When generating a message from a diff or summary:

1. Identify the single main purpose of the change.
2. Choose the most specific type and scope.
3. Write the first line from the user's outcome, not the file names.
4. Add a body only if it adds context that a reviewer or future maintainer would not infer from the first line.
5. Mention risks, migrations, and compatibility implications when present.

If a change mixes unrelated concerns, say so and suggest splitting the work before writing a single message.

## Examples

Simple mechanical change:

```text
chore(deps): bump example-lib to 1.4.2
```

Bug fix with useful context:

```text
fix(auth): reject expired refresh tokens before lookup

Move refresh-token expiry validation before session lookup so invalid
requests fail without unnecessary database work.

This also makes the error path match access-token validation.
```

Refactor with intent:

```text
refactor(pipeline): separate archive writes from fetch orchestration

Move atomic write handling behind a small archive boundary so fetch retry
logic can stay focused on network behavior.
```

Documentation-only change:

```text
docs(usage): document scraper recovery flow
```

Poor messages to rewrite:

```text
fix: stuff
update
wip
chore(misc): changes
```

## Validation Script

This skill bundles `scripts/change_message.py` for deterministic validation.

Use it when the user asks to validate a message or when you want a quick structure check:

```bash
python scripts/change_message.py validate "fix(auth): reject expired refresh tokens before lookup"
```

Or pass a multiline message on stdin:

```bash
python scripts/change_message.py validate < message.txt
```

The validator intentionally enforces only the minimum structure:

- first line matches `type(scope): summary`
- type is in the allowed list
- scope and summary are non-empty
- summary is not too long
- summary is not obviously vague
- summary does not end with a period

The validator does not require a body. The skill should still recommend one when the change needs context.
