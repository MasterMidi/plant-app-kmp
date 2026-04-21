# Development Ideas & Brainstorming

A collection of ideas, experiments, and nice-to-haves that don't yet have a
concrete plan. Pick from here when looking for the next thing to build.

---

## UI / UX Ideas

- **Home-screen widget** (Android) / Lock-screen widget (iOS 16+) showing
  today's check-in progress and top predictions.
- **Wear OS / watchOS companion** — quick "watered" logging from the wrist
  while standing at the plant.
- **Dark mode + plant-themed colour palettes** — e.g. "Monstera Green",
  "Desert Sunset" for cactus lovers.
- **Onboarding flow** — guided first-plant creation with species suggestions
  and a short tutorial on the check-in concept.
- **Drag-and-drop reorder** for plants on the list / check-in screen.
- **Haptic feedback** on check-in completion — satisfying micro-interactions.

## Gamification

- **Streaks** — track consecutive daily check-ins; show a streak counter.
- **Plant health score** — a composite score per plant based on care
  consistency, prediction accuracy, and user-reported health.
- **Achievements / badges** — "First 30-day streak", "Fed all plants this
  month", "Zero overdue predictions for a week".
- **Seasonal challenges** — "Spring repotting challenge: repot 3 plants".

## Data & Integrations

- **Barcode / QR scanning** for fertiliser and pesticide products — auto-fill
  product name and link to usage history.
- **Integration with plant databases** (e.g. Trefle API, USDA plant database)
  for species-specific care info, toxicity warnings, and growth habits.
- **Seasonal care calendar** — auto-generated per-plant care schedule based on
  species, location, and hemisphere.
- **Import / export** — bulk import plants from a CSV; export full history for
  backup or analysis.
- **Notion / Obsidian integration** — sync plant notes to a knowledge base.

## Technical Experiments

- **Compose Multiplatform desktop target** — add a JVM desktop version for
  managing the collection on a laptop.
- **Compose for Web (Wasm)** — a web dashboard for viewing stats without
  installing the app.
- **Room vs. SQLDelight benchmark** — evaluate both for the shared DB layer;
  currently leaning SQLDelight but Room has better Android tooling.
- **On-device model training** — retrain a lightweight model directly on the
  device using new data (Core ML supports this on iOS; TFLite has limited
  support on Android).
- **Kotlin/Native + SwiftUI interop** — experiment with calling Compose from
  SwiftUI and vice versa for screens that need deep platform integration.
- **KMP shared ViewModel** — share ViewModels across platforms using
  `lifecycle-viewmodel-compose` (already in dependencies).

## Hardware / IoT

- **Custom soil sensor design** — ESP32-C3 + capacitive soil moisture probe +
  BME280 (temp/humidity) + BH1750 (light). Communicate via ESPHome → Home
  Assistant → app.
- **NFC plant tags** — tap an NFC sticker on the pot to jump to that plant's
  detail screen; use NFC ID as a plant identifier.
- **Bluetooth plant sensors** — integrate with existing commercial sensors
  (Xiaomi Flora, Parrot Pot) to read soil moisture, temperature, light,
  and conductivity.
- **Automated watering** — long-term dream: close the loop with a
  solenoid-valve irrigation system controlled by HA, triggered by app
  predictions + sensor data.

## Content & Knowledge

- **Built-in plant care wiki** — species-level articles with watering,
  light, and feeding guides.
- **"What's wrong with my plant?"** — a guided troubleshooting flow:
  symptom checklist → likely causes → suggested actions.
- **Propagation tracker** — log propagation attempts (cuttings, division,
  air-layering) and track success rates.

## Accessibility & Localisation

- **VoiceOver / TalkBack support** — ensure all screens are fully accessible.
- **Localisation** — start with English, then add Dutch, German, Spanish.
- **Large text / high-contrast mode** — beyond system defaults, with
  plant-friendly colour choices.

---

*Add new ideas freely. Move items to `mvp-plan.md` or `future-enhancements.md`
when they graduate from brainstorming to planned work.*
