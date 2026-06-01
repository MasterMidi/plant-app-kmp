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
import org.michael.plantapp.data.local.PlantDatabaseFactory
import org.michael.plantapp.data.local.PlantLocalDataSource
import org.michael.plantapp.model.Pest
import org.michael.plantapp.model.PestId
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.PlantId
import org.michael.plantapp.model.SampleKnownPlants
import org.michael.plantapp.navigation.PlantDraft
import org.michael.plantapp.navigation.PlantFormRoute
import org.michael.plantapp.navigation.PlantRoute
import org.michael.plantapp.navigation.toDraft
import org.michael.plantapp.navigation.toRoute
import org.michael.plantapp.navigation.withSelectedKnownPlant
import org.michael.plantapp.ui.KnownPlantSearchScreen
import org.michael.plantapp.ui.PlantFormScreen
import org.michael.plantapp.ui.PlantListScreen
import org.michael.plantapp.ui.SettingsScreen
import org.michael.plantapp.viewmodel.PlantListState
import org.michael.plantapp.viewmodel.PlantListViewModel

@Composable
fun App(
    databaseFactory: PlantDatabaseFactory? = null,
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    val colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        val plantLocalDataSource = remember(databaseFactory) {
            databaseFactory
                ?.createDatabase()
                ?.let(::PlantLocalDataSource)
        }
        val viewModel = viewModel {
            PlantListViewModel(plantLocalDataSource)
        }
        val plantListState by viewModel.state.collectAsStateWithLifecycle()

        PlantAppContent(
            plantListState = plantListState,
            isDarkTheme = isDarkTheme,
            onThemeChange = { isDarkTheme = it },
            onAddPlant = viewModel::addPlant,
            onUpdatePlant = viewModel::updatePlant,
            onCreatePest = viewModel::addPest,
            onWaterPlant = viewModel::waterPlant,
            onDeletePlant = viewModel::deletePlant,
        )
    }
}

@Composable
private fun PlantAppContent(
    plantListState: PlantListState,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onAddPlant: (name: String, scientificName: String, knownPlantId: String?, pestIds: List<PestId>) -> Unit,
    onUpdatePlant: (Plant) -> Unit,
    onCreatePest: (name: String, description: String) -> Pest,
    onWaterPlant: (PlantId) -> Unit,
    onDeletePlant: (PlantId) -> Unit,
) {
    var route by remember { mutableStateOf<PlantRoute>(PlantRoute.List) }

    when (val currentRoute = route) {
        PlantRoute.List -> PlantListScreen(
            state = plantListState,
            onCreatePlant = { route = PlantRoute.Create() },
            onEditPlant = { plant -> route = PlantRoute.Edit(plant.id) },
            onWaterPlant = onWaterPlant,
            onDeletePlant = onDeletePlant,
            onOpenSettings = { route = PlantRoute.Settings },
        )

        is PlantRoute.Create -> PlantFormScreen(
            initialName = currentRoute.draft.name,
            initialScientificName = currentRoute.draft.scientificName,
            initialKnownPlantId = currentRoute.draft.knownPlantId,
            initialPestIds = currentRoute.draft.pestIds,
            availablePests = plantListState.pests,
            onCreatePest = onCreatePest,
            onSave = { name, scientificName, knownPlantId, pestIds ->
                onAddPlant(name, scientificName, knownPlantId, pestIds)
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
                    onWaterPlant = onWaterPlant,
                    onDeletePlant = onDeletePlant,
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
                    onCreatePest = onCreatePest,
                    onSave = { name, scientificName, knownPlantId, pestIds ->
                        onUpdatePlant(
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
            onThemeChange = onThemeChange,
            onBack = { route = PlantRoute.List },
        )
    }
}
