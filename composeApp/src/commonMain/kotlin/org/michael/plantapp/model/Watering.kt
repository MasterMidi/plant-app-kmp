package org.michael.plantapp.model

import kotlin.time.Instant

typealias WateringId = Long

data class Watering(
    val id: WateringId,
    val plantId: PlantId,
    val wateredAt: Instant,
    val intensity: WateringIntensity = WateringIntensity.Moderate,
    val notes: String = "",
)

enum class WateringIntensity {
    Light,
    Moderate,
    DeepSoak,
}

data class PlantWateringSummary(
    val lastWateredAt: Instant? = null,
    val wateringCount: Int = 0,
)

fun Iterable<Watering>.summariesByPlant(): Map<PlantId, PlantWateringSummary> =
    groupBy { it.plantId }.mapValues { (_, waterings) ->
        PlantWateringSummary(
            lastWateredAt = waterings.maxOfOrNull { it.wateredAt },
            wateringCount = waterings.size,
        )
    }
