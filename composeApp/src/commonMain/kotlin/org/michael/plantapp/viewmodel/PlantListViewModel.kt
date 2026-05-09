package org.michael.plantapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import org.michael.plantapp.model.Plant

class PlantListViewModel : ViewModel() {
    private var nextId = 1L

    private val _plants = mutableStateListOf<Plant>()
    val plants: List<Plant> get() = _plants

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

    fun deletePlant(id: Long) {
        _plants.removeAll { it.id == id }
    }
}
