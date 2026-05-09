package org.michael.plantapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.michael.plantapp.model.SampleKnownPlants
import org.michael.plantapp.model.KnownPlant
import org.michael.plantapp.model.Plant
import org.michael.plantapp.ui.KnownPlantSearchScreen
import org.michael.plantapp.ui.PlantFormScreen
import org.michael.plantapp.ui.PlantListScreen
import org.michael.plantapp.viewmodel.PlantListViewModel

@Composable
fun App() {
    MaterialTheme {
        val viewModel = viewModel { PlantListViewModel() }
        var route by remember { mutableStateOf<PlantRoute>(PlantRoute.List) }

        when (val currentRoute = route) {
            PlantRoute.List -> PlantListScreen(
                viewModel = viewModel,
                onCreatePlant = { route = PlantRoute.Create() },
                onEditPlant = { plant -> route = PlantRoute.Edit(plant.id) },
            )

            is PlantRoute.Create -> PlantFormScreen(
                initialName = currentRoute.draft.name,
                initialScientificName = currentRoute.draft.scientificName,
                initialKnownPlantScientificName = currentRoute.draft.knownPlantScientificName,
                onSave = { name, scientificName, knownPlantScientificName ->
                    viewModel.addPlant(name, scientificName, knownPlantScientificName)
                    route = PlantRoute.List
                },
                onCancel = { route = PlantRoute.List },
                onSearchKnownPlant = { name, scientificName, knownPlantScientificName ->
                    route = PlantRoute.SelectKnownPlant(
                        returnTo = PlantFormRoute.Create(
                            PlantDraft(
                                name = name,
                                scientificName = scientificName,
                                knownPlantScientificName = knownPlantScientificName,
                            ),
                        ),
                    )
                },
            )

            is PlantRoute.Edit -> {
                val plant = viewModel.plants.firstOrNull { it.id == currentRoute.plantId }

                if (plant == null) {
                    PlantListScreen(
                        viewModel = viewModel,
                        onCreatePlant = { route = PlantRoute.Create() },
                        onEditPlant = { selectedPlant -> route = PlantRoute.Edit(selectedPlant.id) },
                    )
                } else {
                    val draft = currentRoute.draft ?: plant.toDraft()

                    PlantFormScreen(
                        existing = plant,
                        initialName = draft.name,
                        initialScientificName = draft.scientificName,
                        initialKnownPlantScientificName = draft.knownPlantScientificName,
                        onSave = { name, scientificName, knownPlantScientificName ->
                            viewModel.updatePlant(
                                plant.copy(
                                    name = name.trim(),
                                    scientificName = scientificName.trim(),
                                    knownPlantScientificName = knownPlantScientificName?.takeIf { it.isNotBlank() },
                                ),
                            )
                            route = PlantRoute.List
                        },
                        onCancel = { route = PlantRoute.List },
                        onSearchKnownPlant = { name, scientificName, knownPlantScientificName ->
                            route = PlantRoute.SelectKnownPlant(
                                returnTo = PlantFormRoute.Edit(
                                    plantId = plant.id,
                                    draft = PlantDraft(
                                        name = name,
                                        scientificName = scientificName,
                                        knownPlantScientificName = knownPlantScientificName,
                                    ),
                                ),
                            )
                        },
                    )
                }
            }

            is PlantRoute.SelectKnownPlant -> KnownPlantSearchScreen(
                plants = SampleKnownPlants,
                onSelectPlant = { knownPlant ->
                    route = currentRoute.returnTo.withSelectedKnownPlant(knownPlant)
                },
                onCancel = {
                    route = currentRoute.returnTo.toRoute()
                },
            )
        }
    }
}

private sealed interface PlantRoute {
    data object List : PlantRoute
    data class Create(val draft: PlantDraft = PlantDraft()) : PlantRoute
    data class Edit(val plantId: Long, val draft: PlantDraft? = null) : PlantRoute
    data class SelectKnownPlant(val returnTo: PlantFormRoute) : PlantRoute
}

private sealed interface PlantFormRoute {
    data class Create(val draft: PlantDraft) : PlantFormRoute
    data class Edit(val plantId: Long, val draft: PlantDraft) : PlantFormRoute
}

private data class PlantDraft(
    val name: String = "",
    val scientificName: String = "",
    val knownPlantScientificName: String? = null,
)

private fun Plant.toDraft() = PlantDraft(
    name = name,
    scientificName = scientificName,
    knownPlantScientificName = knownPlantScientificName,
)

private fun PlantFormRoute.toRoute(): PlantRoute = when (this) {
    is PlantFormRoute.Create -> PlantRoute.Create(draft)
    is PlantFormRoute.Edit -> PlantRoute.Edit(plantId, draft)
}

private fun PlantFormRoute.withSelectedKnownPlant(knownPlant: KnownPlant): PlantRoute = when (this) {
    is PlantFormRoute.Create -> PlantRoute.Create(draft.withSelectedKnownPlant(knownPlant))
    is PlantFormRoute.Edit -> PlantRoute.Edit(plantId, draft.withSelectedKnownPlant(knownPlant))
}

private fun PlantDraft.withSelectedKnownPlant(knownPlant: KnownPlant) = copy(
    name = name.ifBlank { knownPlant.commonName },
    scientificName = knownPlant.scientificName,
    knownPlantScientificName = knownPlant.scientificName,
)
