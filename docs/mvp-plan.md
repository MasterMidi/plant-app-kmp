# MVP Plan — Plant App

## Overview

**Plant App** is a mobile application for managing an indoor plant collection.
It is built with **Kotlin Multiplatform** and **Compose Multiplatform**, sharing
both business logic and UI between Android and iOS.

The core idea is a **daily check-in** workflow: each day the user reviews their
plants, logs any care activities, and the app learns from this data to predict
when each plant will next need attention.

---

## Core Principles

1. **Daily check-in first** — the app is designed around a short, consistent
   daily routine. This produces high-quality, regularly-spaced data that makes
   ML predictions far more reliable.
2. **Offline-first** — all data is stored locally on the device. No account or
   internet connection is required.
3. **Shared UI** — a single Compose Multiplatform UI runs on both Android and
   iOS to minimise duplication.

---

## MVP Features

### 1. Plant Inventory

- Add a plant with: name, species/variety (free text), location (room/area),
  photo (optional), date acquired.
- Edit and delete plants.
- List view with search/filter by name or location.

### 2. Daily Check-In

- A dedicated screen that presents each plant for review.
- For each plant the user can:
  - Mark "all good" (no action needed — still records that the plant was
    checked).
  - Log one or more care activities (see below).
  - Skip (come back to it later today).
- A progress indicator shows how many plants have been reviewed today.

### 3. Care Activity Logging

Supported activity types for MVP:

| Activity | Extra Fields |
|---|---|
| **Watering** | amount (light / normal / heavy) |
| **Feeding** | fertiliser name (free text), concentration |
| **Washing** | — |
| **Pruning** | notes |
| **Repotting** | new pot size, soil mix notes |
| **Pesticide** | product name, reason |
| **Other** | free-text note |

Each log entry records: plant, activity type, timestamp, and any extra fields.

### 4. Plant History / Timeline

- Per-plant detail screen showing a chronological timeline of all care events.
- Summary stats: days since last watering, feeding, etc.

### 5. Basic ML Predictions

- Use on-device ML (Core ML on iOS, ML Kit / TensorFlow Lite on Android) to
  predict the next date for each care activity per plant.
- Initial model: a simple time-series approach based on intervals between past
  events of the same type.
- Display predictions on the daily check-in screen ("likely needs water today")
  and on the plant detail screen.
- Predictions improve as more data is collected — the app should clearly
  communicate confidence levels (e.g. "not enough data yet").

### 6. Notifications / Reminders

- A single daily reminder at a configurable time: "Time to check your plants!"
- No per-plant notifications in MVP — the check-in flow handles prioritisation.

---

## Data Model (MVP)

```
Plant
  id            : UUID
  name          : String
  species       : String?
  location      : String?
  photoUri      : String?
  dateAcquired  : LocalDate?
  createdAt     : Instant
  updatedAt     : Instant

CareEvent
  id            : UUID
  plantId       : UUID  (FK → Plant)
  type          : CareType (enum: WATERING, FEEDING, WASHING, PRUNING,
                            REPOTTING, PESTICIDE, CHECK, OTHER)
  timestamp     : Instant
  notes         : String?

  -- type-specific fields stored as nullable columns or a JSON blob --
  amount        : String?      (for WATERING: light/normal/heavy)
  productName   : String?      (for FEEDING, PESTICIDE)
  concentration : String?      (for FEEDING)
  potSize       : String?      (for REPOTTING)
  soilMix       : String?      (for REPOTTING)
  reason        : String?      (for PESTICIDE)

DailyCheckIn
  id            : UUID
  date          : LocalDate
  completedAt   : Instant?
  plantCount    : Int          (total plants that day)
  checkedCount  : Int          (plants reviewed)
```

### Storage

- **SQLDelight** for the shared multiplatform database layer (generates
  type-safe Kotlin from SQL).
- Single local SQLite database on each platform.

---

## Screen Map

```
┌─────────────────┐
│   Home / Today   │  ← daily check-in entry point + prediction summary
├─────────────────┤
│  Check-In Flow   │  ← swipe through plants, log activities
├─────────────────┤
│  Plant List      │  ← browse / search all plants
├─────────────────┤
│  Plant Detail    │  ← info, timeline, predictions for one plant
├─────────────────┤
│  Add / Edit      │  ← plant form
├─────────────────┤
│  Settings        │  ← reminder time, theme, about
└─────────────────┘
```

---

## Technical Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.x (Multiplatform) |
| UI | Compose Multiplatform (Material 3) |
| Navigation | Compose Navigation (multiplatform) |
| Database | SQLDelight |
| DI | Koin Multiplatform |
| ML (Android) | ML Kit or TensorFlow Lite |
| ML (iOS) | Core ML |
| Image loading | Coil 3 (multiplatform) |
| Date/Time | kotlinx-datetime |
| Serialization | kotlinx-serialization |
| Build | Gradle with version catalogs |

---

## Success Criteria

The MVP is "done" when a user can:

1. Add plants to their collection.
2. Complete a daily check-in for all plants.
3. Log any care activity with relevant details.
4. View the history timeline for each plant.
5. See a basic ML prediction for next watering on each plant.
6. Receive a daily reminder notification.
7. Do all of the above on both Android and iOS with a shared UI.
