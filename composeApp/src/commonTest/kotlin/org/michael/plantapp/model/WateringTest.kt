package org.michael.plantapp.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class WateringTest {
    @Test
    fun summariesByPlantKeepsWateringsSeparateFromPlants() {
        val waterings = listOf(
            Watering(
                id = 1L,
                plantId = 1L,
                wateredAt = Instant.parse("2026-05-01T10:00:00Z"),
            ),
            Watering(
                id = 2L,
                plantId = 1L,
                wateredAt = Instant.parse("2026-05-03T10:00:00Z"),
            ),
            Watering(
                id = 3L,
                plantId = 2L,
                wateredAt = Instant.parse("2026-05-02T10:00:00Z"),
            ),
        )

        val summaries = waterings.summariesByPlant()

        assertEquals(2, summaries[1L]?.wateringCount)
        assertEquals(Instant.parse("2026-05-03T10:00:00Z"), summaries[1L]?.lastWateredAt)
        assertEquals(1, summaries[2L]?.wateringCount)
    }
}
