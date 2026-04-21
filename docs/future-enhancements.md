# Future Enhancements

Features and improvements planned after the MVP is complete.

---

## 1. Weather-Aware Predictions

- Integrate historical weather forecast data for the user's location
  (temperature, humidity, sunlight hours, season).
- Feed weather features into the ML model so predictions adapt to environmental
  conditions (e.g. plants need more water during heatwaves, less in winter).
- Potential data sources: Open-Meteo (free, no API key), OpenWeatherMap.

## 2. Home Assistant Integration

- Connect to a Home Assistant instance on the local network.
- Read data from **custom soil moisture sensors** (e.g. ESP32-based capacitive
  sensors reporting via MQTT → HA).
- Use sensor readings (soil moisture, ambient temperature, light level) as
  additional inputs to the ML model.
- Optional: trigger HA automations (e.g. turn on a grow light when a plant
  needs more light).

## 3. Plant Identification via Camera

- Point the camera at a plant and use on-device ML to suggest the species.
- Auto-fill species-specific care defaults (watering frequency, light needs,
  fertiliser schedule) from a built-in knowledge base.
- Could use a fine-tuned model or a cloud API (PlantNet, Google Lens API).

## 4. Push Notifications per Plant

- Move beyond the single daily reminder to individual, prediction-driven alerts:
  "Your Monstera likely needs water today."
- Configurable: per-plant toggle, quiet hours, notification grouping.

## 5. Photo Diary

- Attach time-stamped photos to care events or as standalone diary entries.
- Per-plant photo gallery showing visual growth over time.
- Before/after comparison slider for repotting, pruning, etc.

## 6. Cloud Sync & Backup

- Optional cloud backup (Google Drive / iCloud) so data survives device loss.
- Cross-device sync for users with both a phone and a tablet.
- End-to-end encryption of the backup.

## 7. Advanced Analytics & Charts

- Per-plant charts: watering intervals over time, growth timeline,
  seasonal patterns.
- Collection-wide dashboard: plants by location, care workload heatmap,
  "neediest plants" ranking.
- Export data as CSV or PDF reports.

## 8. Community & Sharing

- Share a plant profile (including care schedule) with other app users.
- Community tips per species.
- "Plant swap" feature for local exchanges.

## 9. Multi-User / Household Support

- Share a plant collection with family members or housemates.
- Assign care responsibilities ("Michael waters the kitchen plants,
  Alex handles the bedroom").
- Activity log shows who did what.

## 10. Enhanced ML Models

- Move from simple interval-based predictions to more sophisticated models:
  - Per-species learned baselines.
  - Seasonal adjustments.
  - Cross-plant correlation (similar plants in the same room).
- Federated learning: improve models across users without sharing raw data.
- Anomaly detection: alert when a plant's care pattern deviates significantly
  from its history (possible sign of trouble).
