package org.michael.plantapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
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
                onCreatePlant = { route = PlantRoute.Create },
                onEditPlant = { plant -> route = PlantRoute.Edit(plant.id) },
            )

            PlantRoute.Create -> PlantFormScreen(
                onSave = { name, scientificName ->
                    viewModel.addPlant(name, scientificName)
                    route = PlantRoute.List
                },
                onCancel = { route = PlantRoute.List },
            )

            is PlantRoute.Edit -> {
                val plant = viewModel.plants.firstOrNull { it.id == currentRoute.plantId }

                if (plant == null) {
                    PlantListScreen(
                        viewModel = viewModel,
                        onCreatePlant = { route = PlantRoute.Create },
                        onEditPlant = { selectedPlant -> route = PlantRoute.Edit(selectedPlant.id) },
                    )
                } else {
                    PlantFormScreen(
                        existing = plant,
                        onSave = { name, scientificName ->
                            viewModel.updatePlant(plant.copy(name = name, scientificName = scientificName))
                            route = PlantRoute.List
                        },
                        onCancel = { route = PlantRoute.List },
                    )
                }
            }
        }
    }
}

private sealed interface PlantRoute {
    data object List : PlantRoute
    data object Create : PlantRoute
    data class Edit(val plantId: Long) : PlantRoute
}
