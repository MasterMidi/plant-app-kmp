package org.michael.plantapp.viewmodel

import org.michael.plantapp.model.BuiltInPests
import org.michael.plantapp.model.Pest
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.PlantId
import org.michael.plantapp.model.PlantWateringSummary
import org.michael.plantapp.model.Watering
import org.michael.plantapp.model.summariesByPlant

data class PlantListState(
    val plants: List<Plant> = emptyList(),
    val pests: List<Pest> = BuiltInPests,
    val waterings: List<Watering> = emptyList(),
) {
    val wateringSummariesByPlant: Map<PlantId, PlantWateringSummary>
        get() = waterings.summariesByPlant()
}
