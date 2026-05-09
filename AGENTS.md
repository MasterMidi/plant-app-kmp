# Coding Agent Instructions

## Version Control: Jujutsu (jj)

This project uses **Jujutsu (jj)** for version control. A `.jj/` directory exists at the repo root. **Never use raw git commands** — they can corrupt the jj state. Use `jj` for all VCS operations (see the jujutsu skill for command reference).

## Commit Strategy: Atomic, Buildable Commits

Every commit in this repository **must compile and build successfully**. No matter where in the history someone checks out, the app must be buildable. This is critical for enabling `jj log` bisection to find regressions.

### Rules

1. **One logical unit of work per commit.** Each commit should represent a single, coherent change — a feature addition, a bug fix, a refactor, a dependency update, etc. If a change touches multiple concerns, split it into separate commits.

2. **Every commit must build.** Before finishing a commit (moving to `jj new`), verify that the project compiles. Never leave the history in a broken state. If a change requires multiple steps that individually break the build, restructure the work so each commit is self-contained and valid.

3. **Prefer smaller commits.** When working on a large task, break it into the smallest meaningful commits that each independently compile and make sense. For example:
   - Adding a new screen might be: data model → repository layer → viewmodel → UI composable → navigation wiring
   - A refactor might be: extract interface → migrate callers one by one → remove old implementation

4. **Describe before coding.** Use `jj desc -m "..."` to set the commit message before writing code. Update it if the scope changes.

5. **Commit messages should be clear and specific.** Use imperative mood, sentence case, no trailing period. Examples:
   - `Add watering schedule notification service`
   - `Extract plant repository interface for testing`
   - `Fix crash when plant image URL is null`

6. **Verify after each commit.** Run the build after completing each unit of work to confirm nothing is broken before moving on to the next commit.

### Why This Matters

- **Bisectability:** Small, buildable commits make it trivial to binary-search for the exact change that introduced a bug.
- **Reviewability:** Each commit tells a clear story and can be reviewed independently.
- **Revertability:** If a change causes problems, it can be cleanly reverted without collateral damage.
- **Time travel:** At any point in history, the project is in a working state — useful for debugging, benchmarking, and understanding evolution.

## Code Organization

- Prefer placing standalone classes, enums, typealiases, and object definitions in their own source files named after the primary type. Keep a type inside another file only when it is a small helper that is tightly scoped to that file or an intentionally nested/subordinate type.
