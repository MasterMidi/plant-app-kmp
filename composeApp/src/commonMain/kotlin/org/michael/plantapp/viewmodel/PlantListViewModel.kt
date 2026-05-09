package org.michael.plantapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlin.time.Instant
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.PlantId
import org.michael.plantapp.model.PlantWateringSummary
import org.michael.plantapp.model.Watering
import org.michael.plantapp.model.WateringIntensity
import org.michael.plantapp.model.summariesByPlant

class PlantListViewModel : ViewModel() {
    private var nextId = 1L
    private var nextWateringId = 1L

    private val _plants = mutableStateListOf<Plant>()
    val plants: List<Plant> get() = _plants

    private val _waterings = mutableStateListOf<Watering>()
    val waterings: List<Watering> get() = _waterings

    val wateringSummariesByPlant: Map<PlantId, PlantWateringSummary>
        get() = _waterings.summariesByPlant()

    fun addPlant(name: String, scientificName: String, knownPlantId: String? = null) {
        _plants.add(
            Plant(
                id = nextId++,
                name = name.trim(),
                scientificName = scientificName.trim(),
                knownPlantId = knownPlantId?.takeIf { it.isNotBlank() },
            ),
        )
    }

    fun updatePlant(plant: Plant) {
        val index = _plants.indexOfFirst { it.id == plant.id }
        if (index >= 0) _plants[index] = plant
    }

    fun addWatering(
        plantId: PlantId,
        wateredAt: Instant,
        intensity: WateringIntensity = WateringIntensity.Moderate,
        notes: String = "",
    ) {
        require(_plants.any { it.id == plantId }) {
            "Cannot add watering for unknown plant"
        }

        _waterings.add(
            Watering(
                id = nextWateringId++,
                plantId = plantId,
                wateredAt = wateredAt,
                intensity = intensity,
                notes = notes.trim(),
            ),
        )
    }

    fun wateringsForPlant(plantId: PlantId): List<Watering> =
        _waterings
            .filter { it.plantId == plantId }
            .sortedByDescending { it.wateredAt }

    fun deletePlant(id: PlantId) {
        _plants.removeAll { it.id == id }
        _waterings.removeAll { it.plantId == id }
    }
}
