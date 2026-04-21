---
name: nix
description: Work with Nix flakes, the Nix language, and the Nix CLI. Use when editing or creating .nix files (flake.nix, shell.nix, default.nix), managing flake inputs/outputs, building derivations, or entering dev shells.
---

# Nix & Flakes Skill

Write, edit, and manage Nix expressions and flakes. This skill covers the Nix language syntax, flake structure, and the `nix` CLI commands for building, developing, and maintaining Nix-based projects.

## Workflow: Working with an Existing Flake

1. **Inspect the flake** with `nix flake show` to see its outputs (packages, devShells, checks, etc.).
2. **Enter a dev shell** with `nix develop` (default) or `nix develop .#name` for a named shell.
3. **Build outputs** with `nix build` or `nix build .#package-name`.
4. **Update inputs** with `nix flake update` (all) or `nix flake update <input>` (single).
5. **Check validity** with `nix flake check` to verify the flake evaluates and passes tests.
6. **Format** with `nix fmt` if a formatter is configured in the flake.

## Workflow: Creating a New Flake

1. **Initialize** with `nix flake init` or `nix flake init -t templates#name`.
2. **Define `inputs`** — pin dependencies (nixpkgs, flake-utils, etc.) with URLs.
3. **Define `outputs`** — a function receiving inputs that returns an attribute set of packages, devShells, checks, overlays, etc.
4. **Lock inputs** with `nix flake lock` to generate/update `flake.lock`.
5. **Test** with `nix flake check` and `nix build`.

## Nix Language Essentials

### Types

```nix
# Primitives
"hello"                        # String
''
  multi-line
  string
''                             # Multi-line string (indentation stripped)
42                             # Integer
3.14                           # Float
true false                     # Booleans
null                           # Null
/path/to/file                  # Path (resolved relative to the file)
./relative/path                # Relative path
```

### Strings & Interpolation

```nix
"Hello, ${name}!"              # String interpolation
''
  ${pkg}/bin/hello             # Also works in multi-line strings
''
```

> Escape `$` in multi-line strings with `''$` (e.g., `''${notInterpolated}`).

### Attribute Sets (Attrsets)

```nix
{
  name = "hello";
  version = "1.0";
  meta.license = "MIT";        # Shorthand for nested: meta = { license = "MIT"; };
}

rec {
  x = 1;
  y = x + 1;                   # `rec` allows self-references
}
```

### Access & Update

```nix
attrs.name                     # Access attribute
attrs.missing or "default"     # Access with default
attrs // { version = "2.0"; }  # Merge/override (right wins)
```

### Let Bindings

```nix
let
  name = "world";
  greeting = "Hello, ${name}!";
in
  greeting
```

### Functions

```nix
x: x + 1                      # Lambda (single arg)
x: y: x + y                   # Curried
{ name, version }:            # Destructured attrset argument
  "${name}-${version}"
{ name, version ? "0.1" }:   # With default value
  "${name}-${version}"
{ name, ... }:                # Allow extra attributes
  name
```

### Lists

```nix
[ 1 2 3 ]                     # List (space-separated, no commas)
[ "a" "b" ] ++ [ "c" ]        # Concatenation
```

### Conditionals

```nix
if condition then "yes" else "no"
```

### Imports

```nix
import ./file.nix              # Evaluate and return the Nix expression in file
import ./file.nix { inherit pkgs; }  # Import and call with args
```

### `inherit`

```nix
{ inherit name version; }      # Equivalent to: { name = name; version = version; }
{ inherit (pkgs) git vim; }    # Equivalent to: { git = pkgs.git; vim = pkgs.vim; }
```

### `with`

```nix
with pkgs; [ git vim curl ]    # Brings attrs into scope (equivalent to [ pkgs.git pkgs.vim pkgs.curl ])
```

### `builtins` (commonly used)

```nix
builtins.attrNames attrs       # List attribute names
builtins.map fn list           # Map over a list
builtins.filter pred list      # Filter a list
builtins.elem x list           # Check membership
builtins.readFile ./path       # Read file as string
builtins.toJSON value          # Convert to JSON string
builtins.fromJSON str          # Parse JSON string
builtins.fetchurl { url; sha256; }  # Fetch a URL
builtins.trace msg val         # Debug print (prints msg, returns val)
```

## Flake Structure

```nix
{
  description = "My project";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in {
        packages.default = pkgs.hello;

        devShells.default = pkgs.mkShell {
          packages = [ pkgs.nodejs pkgs.yarn ];
        };

        checks.default = pkgs.runCommand "test" {} ''
          echo "tests pass" > $out
        '';

        formatter = pkgs.nixfmt-tree;
      }
    );
}
```

### Standard Flake Outputs

| Output | Purpose |
|--------|---------|
| `packages.<system>.<name>` | Buildable packages (`nix build .#name`) |
| `packages.<system>.default` | Default package (`nix build`) |
| `devShells.<system>.<name>` | Dev environments (`nix develop .#name`) |
| `devShells.<system>.default` | Default dev shell (`nix develop`) |
| `checks.<system>.<name>` | CI checks (`nix flake check`) |
| `apps.<system>.<name>` | Runnable apps (`nix run .#name`) |
| `overlays.<name>` | Nixpkgs overlays |
| `nixosModules.<name>` | NixOS modules |
| `formatter.<system>` | Code formatter (`nix fmt`) |
| `templates.<name>` | Flake templates (`nix flake init -t`) |

### Flake Input Types

```nix
inputs = {
  # GitHub repository (most common)
  nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";

  # Specific branch or tag
  nixpkgs.url = "github:NixOS/nixpkgs/release-24.05";

  # Git repository
  my-repo.url = "git+https://example.com/repo.git?ref=main";

  # Local path (useful for development)
  local-dep.url = "path:./subdir";

  # Disable an input's own dependencies
  flake-utils.inputs.nixpkgs.follows = "nixpkgs";
};
```

### `follows` — Deduplicating Inputs

```nix
inputs = {
  nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  some-flake.url = "github:user/some-flake";
  some-flake.inputs.nixpkgs.follows = "nixpkgs";  # Use OUR nixpkgs
};
```

## Common Patterns

### `mkShell` — Dev Shells

```nix
pkgs.mkShell {
  name = "my-project";

  # Build-time dependencies (compilers, build tools)
  nativeBuildInputs = [ pkgs.gcc pkgs.cmake ];

  # Runtime dependencies (libraries)
  buildInputs = [ pkgs.openssl pkgs.zlib ];

  # Shorthand: both native + build inputs
  packages = [ pkgs.nodejs pkgs.python3 ];

  # Environment variables
  DATABASE_URL = "postgres://localhost/dev";

  # Shell hook (bash run on shell entry)
  shellHook = ''
    echo "Welcome to the dev shell"
    export PATH="$PWD/node_modules/.bin:$PATH"
  '';
};
```

### `forAllSystems` — Multi-Platform Support (without flake-utils)

```nix
let
  supportedSystems = [ "x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
  forAllSystems = nixpkgs.lib.genAttrs supportedSystems;
in {
  devShells = forAllSystems (system:
    let pkgs = nixpkgs.legacyPackages.${system};
    in { default = pkgs.mkShell { packages = [ pkgs.hello ]; }; }
  );
}
```

### `buildPythonPackage` / `buildPythonApplication`

```nix
pkgs.python3Packages.buildPythonPackage {
  pname = "my-package";
  version = "1.0.0";
  src = ./.;
  propagatedBuildInputs = with pkgs.python3Packages; [ requests click ];
  nativeCheckInputs = with pkgs.python3Packages; [ pytest ];
}
```

### Overriding Packages

```nix
pkgs.hello.override { stdenv = pkgs.clangStdenv; }   # Override function args
pkgs.hello.overrideAttrs (old: {                      # Override derivation attrs
  patches = old.patches or [] ++ [ ./my-fix.patch ];
})
```

## CLI Quick Reference

| Command | Purpose |
|---------|---------|
| `nix develop` | Enter default dev shell |
| `nix develop .#name` | Enter named dev shell |
| `nix build` | Build default package |
| `nix build .#name` | Build named package |
| `nix build --print-out-paths` | Build and print store path |
| `nix run .#name` | Build and run an app |
| `nix run nixpkgs#package` | Run a package from nixpkgs |
| `nix flake show` | Show all flake outputs |
| `nix flake check` | Validate flake & run checks |
| `nix flake update` | Update all flake inputs |
| `nix flake update <input>` | Update a single input |
| `nix flake lock` | Generate/update lock file |
| `nix flake metadata` | Show flake metadata & inputs |
| `nix flake init` | Initialize a new flake |
| `nix search nixpkgs <term>` | Search for packages |
| `nix eval .#attr` | Evaluate a flake attribute |
| `nix eval --expr '<expr>'` | Evaluate a Nix expression |
| `nix eval --json .#attr` | Evaluate as JSON |
| `nix repl` | Interactive Nix REPL |
| `nix fmt` | Format with flake's formatter |
| `nix log .#package` | Show build log |
| `nix path-info -rsSh .#pkg` | Show closure size |
| `nix why-depends .#a .#b` | Explain dependency chain |
| `nix profile install .#pkg` | Install to user profile |
| `nix store gc` | Garbage collect the store |

## Debugging & Introspection

```bash
# Evaluate and print a value
nix eval .#devShells.x86_64-linux.default.name

# Show the derivation of a flake output
nix derivation show .#package-name

# Enter a REPL with the flake loaded
nix repl .

# Inside the REPL:
#   :l <nixpkgs>     — load nixpkgs
#   :t expr          — show type
#   :p expr          — pretty-print (force evaluation)
#   tab-completion   — explore attrsets

# Trace evaluation (add to nix expression):
builtins.trace "debug: ${toString value}" value
```

## Style & Conventions

- Use 2-space indentation.
- Use `let ... in` to avoid repetition; keep the `in` expression readable.
- Prefer `packages = [ ... ]` over `buildInputs` in `mkShell` for simple tool lists.
- Use `inherit` to reduce noise when passing identically-named bindings.
- Pin nixpkgs to a specific branch (`nixpkgs-unstable`, `release-24.05`) — avoid `main`.
- Always include a `description` in `flake.nix`.
- Use `follows` to deduplicate shared inputs across dependencies.
- Keep `flake.lock` committed to the repository for reproducibility.
- List `supportedSystems` explicitly or use `flake-utils.lib.eachDefaultSystem`.

## References

- [Nix manual — Language](https://nix.dev/manual/nix/latest/language/)
- [Nix manual — CLI](https://nix.dev/manual/nix/latest/command-ref/new-cli/nix)
- [Nix flakes — wiki](https://wiki.nixos.org/wiki/Flakes)
- [Nixpkgs manual](https://nixos.org/manual/nixpkgs/stable/)
- [nix.dev tutorials](https://nix.dev/)
