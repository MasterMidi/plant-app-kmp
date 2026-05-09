package org.michael.plantapp.model

typealias PlantId = Long

data class Plant(
    val id: PlantId,
    val name: String,
    val scientificName: String = "",
    val knownPlantId: String? = null,
    val pestIds: List<PestId> = emptyList(),
)
