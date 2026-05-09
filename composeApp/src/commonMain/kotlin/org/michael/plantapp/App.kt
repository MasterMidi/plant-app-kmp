package org.michael.plantapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.michael.plantapp.model.KnownPlant
import org.michael.plantapp.model.PestId
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.SampleKnownPlants
import org.michael.plantapp.ui.KnownPlantSearchScreen
import org.michael.plantapp.ui.PlantFormScreen
import org.michael.plantapp.ui.PlantListScreen
import org.michael.plantapp.ui.SettingsScreen
import org.michael.plantapp.viewmodel.PlantListViewModel

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }
    val colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        val viewModel = viewModel { PlantListViewModel() }
        val plantListState by viewModel.state.collectAsStateWithLifecycle()
        var route by remember { mutableStateOf<PlantRoute>(PlantRoute.List) }

        when (val currentRoute = route) {
            PlantRoute.List -> PlantListScreen(
                state = plantListState,
                onCreatePlant = { route = PlantRoute.Create() },
                onEditPlant = { plant -> route = PlantRoute.Edit(plant.id) },
                onWaterPlant = viewModel::waterPlant,
                onDeletePlant = viewModel::deletePlant,
                onOpenSettings = { route = PlantRoute.Settings },
            )

            is PlantRoute.Create -> PlantFormScreen(
                initialName = currentRoute.draft.name,
                initialScientificName = currentRoute.draft.scientificName,
                initialKnownPlantId = currentRoute.draft.knownPlantId,
                initialPestIds = currentRoute.draft.pestIds,
                availablePests = plantListState.pests,
                onCreatePest = viewModel::addPest,
                onSave = { name, scientificName, knownPlantId, pestIds ->
                    viewModel.addPlant(name, scientificName, knownPlantId, pestIds)
                    route = PlantRoute.List
                },
                onCancel = { route = PlantRoute.List },
                onSearchKnownPlant = { name, scientificName, knownPlantId, pestIds ->
                    route = PlantRoute.SelectKnownPlant(
                        returnTo = PlantFormRoute.Create(
                            PlantDraft(
                                name = name,
                                scientificName = scientificName,
                                knownPlantId = knownPlantId,
                                pestIds = pestIds,
                            ),
                        ),
                    )
                },
            )

            is PlantRoute.Edit -> {
                val plant = plantListState.plants.firstOrNull { it.id == currentRoute.plantId }

                if (plant == null) {
                    PlantListScreen(
                        state = plantListState,
                        onCreatePlant = { route = PlantRoute.Create() },
                        onEditPlant = { selectedPlant -> route = PlantRoute.Edit(selectedPlant.id) },
                        onWaterPlant = viewModel::waterPlant,
                        onDeletePlant = viewModel::deletePlant,
                        onOpenSettings = { route = PlantRoute.Settings },
                    )
                } else {
                    val draft = currentRoute.draft ?: plant.toDraft()

                    PlantFormScreen(
                        existing = plant,
                        initialName = draft.name,
                        initialScientificName = draft.scientificName,
                        initialKnownPlantId = draft.knownPlantId,
                        initialPestIds = draft.pestIds,
                        availablePests = plantListState.pests,
                        onCreatePest = viewModel::addPest,
                        onSave = { name, scientificName, knownPlantId, pestIds ->
                            viewModel.updatePlant(
                                plant.copy(
                                    name = name.trim(),
                                    scientificName = scientificName.trim(),
                                    knownPlantId = knownPlantId?.takeIf { it.isNotBlank() },
                                    pestIds = pestIds,
                                ),
                            )
                            route = PlantRoute.List
                        },
                        onCancel = { route = PlantRoute.List },
                        onSearchKnownPlant = { name, scientificName, knownPlantId, pestIds ->
                            route = PlantRoute.SelectKnownPlant(
                                returnTo = PlantFormRoute.Edit(
                                    plantId = plant.id,
                                    draft = PlantDraft(
                                        name = name,
                                        scientificName = scientificName,
                                        knownPlantId = knownPlantId,
                                        pestIds = pestIds,
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

            PlantRoute.Settings -> SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeChange = { isDarkTheme = it },
                onBack = { route = PlantRoute.List },
            )
        }
    }
}

private sealed interface PlantRoute {
    data object List : PlantRoute
    data object Settings : PlantRoute
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
    val knownPlantId: String? = null,
    val pestIds: List<PestId> = emptyList(),
)

private fun Plant.toDraft() = PlantDraft(
    name = name,
    scientificName = scientificName,
    knownPlantId = knownPlantId,
    pestIds = pestIds,
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
    knownPlantId = knownPlant.id,
)
