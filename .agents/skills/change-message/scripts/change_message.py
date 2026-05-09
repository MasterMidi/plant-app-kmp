#!/usr/bin/env python3
from __future__ import annotations

import argparse
import re
import sys
from dataclasses import dataclass


ALLOWED_TYPES = {
    "feat",
    "fix",
    "refactor",
    "chore",
    "docs",
    "test",
    "build",
    "ci",
    "perf",
    "revert",
}

VAGUE_SUMMARIES = {
    "change",
    "changes",
    "cleanup",
    "fix",
    "fix stuff",
    "misc",
    "misc cleanup",
    "stuff",
    "update",
    "updates",
    "wip",
    "work in progress",
}

HEADER_RE = re.compile(r"^(?P<type>[a-z]+)\((?P<scope>[^)]+)\): (?P<summary>.+)$")


@dataclass(slots=True)
class ValidationResult:
    errors: list[str]
    warnings: list[str]


def first_nonempty_line(message: str) -> str:
    for line in message.splitlines():
        if line.strip():
            return line.strip()
    return ""


def validate_message(message: str, max_summary_length: int = 72) -> ValidationResult:
    errors: list[str] = []
    warnings: list[str] = []

    first_line = first_nonempty_line(message)
    if not first_line:
        return ValidationResult(errors=["message is empty"], warnings=[])

    match = HEADER_RE.match(first_line)
    if not match:
        return ValidationResult(
            errors=["first line must match: type(scope): summary"],
            warnings=[],
        )

    message_type = match.group("type")
    scope = match.group("scope").strip()
    summary = match.group("summary").strip()

    if message_type not in ALLOWED_TYPES:
        allowed = ", ".join(sorted(ALLOWED_TYPES))
        errors.append(f"type must be one of: {allowed}")

    if not scope:
        errors.append("scope must not be empty")
    elif scope.lower() in {"misc", "stuff", "files", "code"}:
        warnings.append("scope is vague; prefer a component, domain, package, or system")

    if not summary:
        errors.append("summary must not be empty")
    else:
        normalized_summary = re.sub(r"\s+", " ", summary.lower()).strip()
        if len(summary) > max_summary_length:
            errors.append(
                f"summary must be {max_summary_length} characters or fewer "
                f"({len(summary)} found)"
            )
        if summary.endswith("."):
            errors.append("summary must not end with a period")
        if normalized_summary in VAGUE_SUMMARIES:
            errors.append("summary is too vague; describe the concrete outcome")

    return ValidationResult(errors=errors, warnings=warnings)


def read_message(args: argparse.Namespace) -> str:
    if args.message:
        return "\n".join(args.message)
    return sys.stdin.read()


def validate_command(args: argparse.Namespace) -> int:
    result = validate_message(
        read_message(args),
        max_summary_length=args.max_summary_length,
    )

    if result.errors:
        print("invalid change message", file=sys.stderr)
        for error in result.errors:
            print(f"error: {error}", file=sys.stderr)
        for warning in result.warnings:
            print(f"warning: {warning}", file=sys.stderr)
        return 1

    print("valid change message")
    for warning in result.warnings:
        print(f"warning: {warning}", file=sys.stderr)
    return 0


def parse_args(argv: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Validate VCS-agnostic change messages.")
    subparsers = parser.add_subparsers(dest="command", required=True)

    validate = subparsers.add_parser("validate", help="validate a change message")
    validate.add_argument("message", nargs="*", help="message text; stdin is used when omitted")
    validate.add_argument(
        "--max-summary-length",
        type=int,
        default=72,
        help="maximum allowed first-line summary length",
    )
    validate.set_defaults(func=validate_command)

    return parser.parse_args(argv)


def main(argv: list[str] | None = None) -> int:
    args = parse_args(sys.argv[1:] if argv is None else argv)
    return args.func(args)


if __name__ == "__main__":
    raise SystemExit(main())
