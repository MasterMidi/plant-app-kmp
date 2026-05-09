package org.michael.plantapp.navigation

import org.michael.plantapp.model.KnownPlant
import org.michael.plantapp.model.PlantId

internal sealed interface PlantFormRoute {
    data class Create(val draft: PlantDraft) : PlantFormRoute
    data class Edit(val plantId: PlantId, val draft: PlantDraft) : PlantFormRoute
}

internal fun PlantFormRoute.toRoute(): PlantRoute = when (this) {
    is PlantFormRoute.Create -> PlantRoute.Create(draft)
    is PlantFormRoute.Edit -> PlantRoute.Edit(plantId, draft)
}

internal fun PlantFormRoute.withSelectedKnownPlant(knownPlant: KnownPlant): PlantRoute = when (this) {
    is PlantFormRoute.Create -> PlantRoute.Create(draft.withSelectedKnownPlant(knownPlant))
    is PlantFormRoute.Edit -> PlantRoute.Edit(plantId, draft.withSelectedKnownPlant(knownPlant))
}
