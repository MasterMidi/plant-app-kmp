package org.michael.plantapp.navigation

import org.michael.plantapp.model.PlantId

internal sealed interface PlantRoute {
    data object List : PlantRoute
    data object Settings : PlantRoute
    data class Create(val draft: PlantDraft = PlantDraft()) : PlantRoute
    data class Edit(val plantId: PlantId, val draft: PlantDraft? = null) : PlantRoute
    data class SelectKnownPlant(val returnTo: PlantFormRoute) : PlantRoute
}
