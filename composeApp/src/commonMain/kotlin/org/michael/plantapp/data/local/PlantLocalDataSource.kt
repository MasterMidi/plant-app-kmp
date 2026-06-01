package org.michael.plantapp.data.local

import org.michael.plantapp.db.PlantDatabase
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.PlantId

class PlantLocalDataSource(private val database: PlantDatabase) {
    private val queries = database.plantsQueries

    fun savePlant(name: String): Plant {
        val trimmedName = name.trim()
        queries.insertNewPlant(name = trimmedName)
        val id = queries.lastInsertRowId().executeAsOne()
        return Plant(id = id, name = trimmedName)
    }

    fun savePlant(plant: Plant): Plant {
        val savedPlant = Plant(
            id = plant.id,
            name = plant.name.trim(),
        )
        queries.insertExistingPlant(
            id = savedPlant.id,
            name = savedPlant.name,
        )
        return savedPlant
    }

    fun getAllPlants(): List<Plant> {
        return queries
            .selectAllPlants()
            .executeAsList()
            .map { row ->
                Plant(
                    id = row.id,
                    name = row.name,
                )
            }
    }

    fun getPlant(id: PlantId): Plant? {
        return queries
            .selectPlantById(id = id)
            .executeAsOneOrNull()
            ?.let { row ->
                Plant(
                    id = row.id,
                    name = row.name,
                )
            }
    }

    fun deletePlant(id: PlantId) {
        queries.deletePlant(id = id)
    }
}
