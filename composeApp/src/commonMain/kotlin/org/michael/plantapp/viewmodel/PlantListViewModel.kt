package org.michael.plantapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import org.michael.plantapp.data.local.PlantLocalDataSource
import org.michael.plantapp.model.Pest
import org.michael.plantapp.model.PestId
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.PlantId
import org.michael.plantapp.model.Watering
import org.michael.plantapp.model.WateringIntensity

class PlantListViewModel(
    private val plantLocalDataSource: PlantLocalDataSource? = null,
) : ViewModel() {
    private var nextPestId = 1L
    private var nextWateringId = 1L

    private val _state = MutableStateFlow(PlantListState())
    val state: StateFlow<PlantListState> = _state.asStateFlow()

    init {
        plantLocalDataSource?.let { dataSource ->
            _state.update { state ->
                state.copy(plants = dataSource.getAllPlants())
            }
        }
    }

    fun addPlant(
        name: String,
        scientificName: String,
        knownPlantId: String? = null,
        pestIds: List<PestId> = emptyList(),
    ) {
        val savedPlant = plantLocalDataSource?.savePlant(name) ?: Plant(
            id = nextPreviewPlantId(),
            name = name.trim(),
        )

        _state.update { state ->
            state.copy(
                plants = state.plants + savedPlant,
            )
        }
    }

    private fun nextPreviewPlantId(): PlantId =
        (state.value.plants.maxOfOrNull { it.id } ?: 0L) + 1L

    fun addPest(name: String, description: String): Pest {
        val pest = Pest(
            id = "custom-pest-${nextPestId++}",
            name = name.trim(),
            description = description.trim(),
        )
        _state.update { state -> state.copy(pests = state.pests + pest) }
        return pest
    }

    fun knownPestIds(pestIds: List<PestId>): List<PestId> =
        pestIds.distinct().filter { id -> state.value.pests.any { it.id == id } }

    fun updatePlant(plant: Plant) {
        val savedPlant = plantLocalDataSource?.savePlant(plant) ?: plant.copy(
            name = plant.name.trim(),
            scientificName = plant.scientificName.trim(),
            knownPlantId = plant.knownPlantId?.takeIf { it.isNotBlank() },
            pestIds = knownPestIds(plant.pestIds),
        )

        _state.update { state ->
            state.copy(
                plants = state.plants.map { existingPlant ->
                    if (existingPlant.id == plant.id) {
                        savedPlant
                    } else {
                        existingPlant
                    }
                },
            )
        }
    }

    fun addWatering(
        plantId: PlantId,
        wateredAt: Instant,
        intensity: WateringIntensity = WateringIntensity.Moderate,
        notes: String = "",
    ) {
        require(state.value.plants.any { it.id == plantId }) {
            "Cannot add watering for unknown plant"
        }

        _state.update { state ->
            state.copy(
                waterings = state.waterings + Watering(
                    id = nextWateringId++,
                    plantId = plantId,
                    wateredAt = wateredAt,
                    intensity = intensity,
                    notes = notes.trim(),
                ),
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    fun waterPlant(plantId: PlantId, intensity: WateringIntensity = WateringIntensity.Moderate) {
        addWatering(
            plantId = plantId,
            wateredAt = Clock.System.now(),
            intensity = intensity,
        )
    }

    fun wateringsForPlant(plantId: PlantId): List<Watering> =
        state.value.waterings
            .filter { it.plantId == plantId }
            .sortedByDescending { it.wateredAt }

    fun deletePlant(id: PlantId) {
        plantLocalDataSource?.deletePlant(id)
        _state.update { state ->
            state.copy(
                plants = state.plants.filterNot { it.id == id },
                waterings = state.waterings.filterNot { it.plantId == id },
            )
        }
    }
}
