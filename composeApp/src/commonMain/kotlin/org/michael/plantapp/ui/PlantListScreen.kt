package org.michael.plantapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.PlantWateringSummary
import org.michael.plantapp.viewmodel.PlantListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantListScreen(
    viewModel: PlantListViewModel,
    onCreatePlant: () -> Unit,
    onEditPlant: (Plant) -> Unit,
) {
    var deletingPlant by remember { mutableStateOf<Plant?>(null) }
    val wateringSummaries = viewModel.wateringSummariesByPlant
    val plantListItems = viewModel.plants.map { plant ->
        PlantListItem(
            plant = plant,
            wateringSummary = wateringSummaries[plant.id] ?: PlantWateringSummary(),
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My plants") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePlant) {
                Text("+", fontSize = 24.sp)
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (plantListItems.isEmpty()) {
                Text(
                    text = "No plants yet — tap + to add one",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(plantListItems, key = { it.plant.id }) { item ->
                        PlantItem(
                            item = item,
                            onEdit = { onEditPlant(item.plant) },
                            onDeleteRequest = { deletingPlant = item.plant },
                        )
                    }
                }
            }
        }
    }

    deletingPlant?.let { plant ->
        AlertDialog(
            onDismissRequest = { deletingPlant = null },
            title = { Text("Delete plant?") },
            text = { Text("\"${plant.name}\" will be permanently removed.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePlant(plant.id)
                    deletingPlant = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { deletingPlant = null }) { Text("Cancel") }
            },
        )
    }
}

private data class PlantListItem(
    val plant: Plant,
    val wateringSummary: PlantWateringSummary = PlantWateringSummary(),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PlantItem(
    item: PlantListItem,
    onEdit: () -> Unit,
    onDeleteRequest: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    val plant = item.plant
    val wateringSummary = item.wateringSummary

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onEdit()
            }
            false // returning false snaps back automatically; we never commit the dismiss
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                ) {
                    Text("Edit")
                }
            }
        },
    ) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { showMenu = true },
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = plant.name,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (plant.scientificName.isNotBlank()) {
                        Text(
                            text = plant.scientificName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (wateringSummary.lastWateredAt != null) {
                        Text(
                            text = "Last watered ${wateringSummary.lastWateredAt}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        showMenu = false
                        onDeleteRequest()
                    },
                )
            }
        }
    }
}
