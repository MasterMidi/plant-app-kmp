# Development Environment Setup

This project uses **Nix flakes** to provide a fully reproducible development
environment with the Android SDK, JDK, emulator, and helper commands — no
Android Studio required.

---

## Prerequisites

### 1. NixOS with Flakes Enabled

Add the following to your NixOS configuration (`/etc/nixos/configuration.nix`):

```nix
nix.settings.experimental-features = [ "nix-command" "flakes" ];
```

### 2. KVM Access (for the Android Emulator)

The emulator needs hardware acceleration via KVM.  Make sure your user is in
the `kvm` group:

```nix
# In your NixOS configuration
users.users.<your-username>.extraGroups = [ "kvm" ];
```

Rebuild and log out / log in for the group change to take effect.

### 3. ADB & USB Debugging (for Real Devices)

To deploy to a physical Android device over USB:

```nix
# In your NixOS configuration
programs.adb.enable = true;
users.users.<your-username>.extraGroups = [ "adbusers" ];
```

Then on your Android phone/tablet:
1. Go to **Settings → About Phone** and tap **Build Number** 7 times to enable
   Developer Options.
2. Go to **Settings → Developer Options** and enable **USB Debugging**.
3. Connect via USB — approve the debugging prompt on the device.

### 4. (Recommended) direnv + nix-direnv

For automatic shell activation when you `cd` into the project:

```nix
# In your NixOS configuration or home-manager
programs.direnv.enable = true;
programs.direnv.nix-direnv.enable = true;
```

After installing, run once in the project root:

```bash
direnv allow
```

---

## Getting Started

### Enter the Development Shell

```bash
cd /path/to/plant-app

# Option A: manual
nix develop

# Option B: automatic via direnv (if configured above)
# Just cd into the directory — the shell activates automatically.
```

You should see the Plant App welcome message and a list of available commands.

### Verify the Environment

```bash
check-env          # Shows SDK paths, tools, Java version, KVM status
```

### Configure local.properties

```bash
setup-sdk          # Writes sdk.dir pointing to the Nix Android SDK
```

> **Note:** `local.properties` is in `.gitignore` — each developer generates
> their own with `setup-sdk`.

### First Build

```bash
build-debug        # Equivalent to ./gradlew :composeApp:assembleDebug
```

The first build downloads Gradle dependencies and may take several minutes.
Subsequent builds are much faster.

---

## Running on the Android Emulator

```bash
# 1. Create a virtual device (one-time setup)
create-avd

# 2. Start the emulator
start-emulator

# 3. Wait for boot, then deploy
adb wait-for-device
run-app
```

### Emulator Tips

| Issue | Fix |
|---|---|
| Emulator is very slow | Ensure `/dev/kvm` is writable (`check-env` will warn) |
| Black screen / GPU errors | Try `start-emulator -- -gpu swiftshader_indirect` |
| No audio | `libpulseaudio` and `alsa-lib` are included; check PulseAudio config |

### Custom AVDs

```bash
# List available device profiles
avdmanager list device

# Create a custom AVD (e.g., Pixel 8 Pro)
avdmanager create avd \
  --name "pixel8pro" \
  --package "system-images;android-35;google_apis;x86_64" \
  --device "pixel_8_pro" \
  --force

# Start your custom AVD
emulator -avd pixel8pro -gpu auto &
```

---

## Running on a Physical Device

1. Complete the USB debugging setup in [Prerequisites](#3-adb--usb-debugging-for-real-devices).
2. Connect the device.
3. Verify the connection:

```bash
list-devices       # Should show your device
```

4. Deploy:

```bash
run-app            # Builds, installs, and launches on the connected device
```

### Wireless Debugging (Android 11+)

```bash
# On the device: Settings → Developer Options → Wireless Debugging → Pair
adb pair <ip>:<port>        # Enter the pairing code
adb connect <ip>:<port>     # Connect for deployment
run-app
```

---

## Debugging

### Logcat

```bash
logcat                      # Filtered to the plant-app process
logcat -- -s "MyTag"        # Filter by tag
logcat -- "*:E"             # Errors only
```

### Gradle Debug Flags

```bash
./gradlew :composeApp:assembleDebug --info       # Verbose Gradle output
./gradlew :composeApp:assembleDebug --stacktrace  # Show full stack traces
```

---

## iOS Development

> **iOS builds require macOS with Xcode.** The shared Kotlin code compiles
> on Linux, but running on an iOS simulator or device is only possible on a Mac.

On macOS:
1. Open `iosApp/iosApp.xcodeproj` in Xcode.
2. Select an iOS simulator or device.
3. Build and run (⌘R).

The shared Compose UI code in `composeApp/src/commonMain/` is used by both
platforms. Platform-specific code lives in `androidMain/` and `iosMain/`.

---

## Available Devshell Commands

Run `menu` inside the devshell to see all commands, grouped by category:

| Category | Command | Description |
|---|---|---|
| **build** | `build-debug` | Build the Android debug APK |
| **build** | `build-release` | Build the Android release APK |
| **android** | `create-avd` | Create a Pixel 6 AVD (API 35) |
| **android** | `list-avds` | List available AVDs |
| **android** | `start-emulator` | Start the emulator |
| **android** | `run-app` | Build, install, and launch on device/emulator |
| **android** | `list-devices` | List connected devices |
| **android** | `logcat` | Filtered logcat for this app |
| **test** | `test-common` | Run shared unit tests |
| **util** | `setup-sdk` | Write local.properties |
| **util** | `check-env` | Verify environment health |
| **util** | `clean` | Clean Gradle build artifacts |

---

## Project Structure

```
plant-app/
├── flake.nix                          # Nix dev environment definition
├── .envrc                             # direnv activation
├── build.gradle.kts                   # Root Gradle config
├── settings.gradle.kts                # Module declarations
├── gradle/
│   └── libs.versions.toml             # Centralized dependency versions
├── composeApp/
│   ├── build.gradle.kts               # KMP + Android + Compose config
│   └── src/
│       ├── commonMain/                # Shared code & Compose UI
│       │   └── kotlin/org/michael/plantapp/
│       ├── commonTest/                # Shared tests
│       ├── androidMain/               # Android-specific code
│       │   ├── AndroidManifest.xml
│       │   └── kotlin/org/michael/plantapp/
│       └── iosMain/                   # iOS-specific code
│           └── kotlin/org/michael/plantapp/
├── iosApp/                            # Xcode project (macOS only)
│   └── iosApp.xcodeproj/
└── docs/                              # Project documentation
```

---

## Updating the Android SDK

When you need a newer API level or build tools:

1. Check available packages:
   ```bash
   nix flake show github:tadfisher/android-nixpkgs/stable
   ```

2. Edit `flake.nix` — update the `android-sdk` package list (e.g. add
   `platforms-android-36`, `build-tools-36-0-0`).

3. Update `gradle/libs.versions.toml` to match the new `compileSdk` /
   `targetSdk`.

4. Run `nix flake update` to refresh the lock file.

5. Re-enter the devshell (`nix develop` or let direnv reload).

6. Run `setup-sdk` and `check-env` to verify.

---

## Troubleshooting

### "SDK location not found"

Run `setup-sdk` to regenerate `local.properties` with the correct Nix SDK path.

### Gradle downloads fail or aapt2 errors

The `GRADLE_OPTS` env var tells AGP to use the Nix-patched `aapt2`. If you see
aapt2-related errors, verify the path exists:

```bash
ls "$ANDROID_HOME/build-tools/35.0.0/aapt2"
```

### Emulator won't start

1. Check KVM: `ls -la /dev/kvm` — your user must have write access.
2. Try software rendering: `emulator -avd plant-app-test -gpu swiftshader_indirect`
3. Check for missing libs: `emulator -avd plant-app-test -verbose 2>&1 | grep "cannot open"`
   — add any missing libraries to `emulatorLibs` in `flake.nix`.

### "ANDROID_HOME not set" inside Gradle

Make sure you're running Gradle from inside the devshell (`nix develop` or
direnv). Don't run `./gradlew` from a plain terminal.
