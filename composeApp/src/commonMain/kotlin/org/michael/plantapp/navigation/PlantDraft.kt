package org.michael.plantapp.navigation

import org.michael.plantapp.model.KnownPlant
import org.michael.plantapp.model.PestId
import org.michael.plantapp.model.Plant

internal data class PlantDraft(
    val name: String = "",
    val scientificName: String = "",
    val knownPlantId: String? = null,
    val pestIds: List<PestId> = emptyList(),
)

internal fun Plant.toDraft() = PlantDraft(
    name = name,
    scientificName = scientificName,
    knownPlantId = knownPlantId,
    pestIds = pestIds,
)

internal fun PlantDraft.withSelectedKnownPlant(knownPlant: KnownPlant) = copy(
    name = name.ifBlank { knownPlant.commonName },
    scientificName = knownPlant.scientificName,
    knownPlantId = knownPlant.id,
)
