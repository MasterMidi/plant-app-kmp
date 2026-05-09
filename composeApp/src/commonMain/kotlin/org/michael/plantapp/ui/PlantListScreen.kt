package org.michael.plantapp.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.michael.plantapp.model.Plant
import org.michael.plantapp.model.PlantWateringSummary
import org.michael.plantapp.viewmodel.PlantListViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantListScreen(
    viewModel: PlantListViewModel,
    onCreatePlant: () -> Unit,
    onEditPlant: (Plant) -> Unit,
    onOpenSettings: () -> Unit,
) {
    var deletingPlant by remember { mutableStateOf<Plant?>(null) }
    var detailItem by remember { mutableStateOf<PlantListItem?>(null) }
    val wateringSummaries = viewModel.wateringSummariesByPlant
    val plantListItems = viewModel.plants.map { plant ->
        PlantListItem(
            plant = plant,
            wateringSummary = wateringSummaries[plant.id] ?: PlantWateringSummary(),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My plants",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Open settings",
                        )
                    }
                },
                expandedHeight = 52.dp,
            )
        },
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
                            onShowDetails = { detailItem = item },
                            onEdit = { onEditPlant(item.plant) },
                            onWater = { viewModel.waterPlant(item.plant.id) },
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

    detailItem?.let { item ->
        PlantDetailsDialog(
            item = item,
            onDismiss = { detailItem = null },
            onEdit = {
                detailItem = null
                onEditPlant(item.plant)
            },
        )
    }
}

private data class PlantListItem(
    val plant: Plant,
    val wateringSummary: PlantWateringSummary = PlantWateringSummary(),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlantItem(
    item: PlantListItem,
    onShowDetails: () -> Unit,
    onEdit: () -> Unit,
    onWater: () -> Unit,
    onDeleteRequest: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    var shouldWater by remember(item.plant.id) { mutableStateOf(false) }
    var actionSwipeOpen by remember(item.plant.id) { mutableStateOf(false) }
    val plant = item.plant
    val wateringSummary = item.wateringSummary
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val actionSize = 64.dp
    val openOffset = 72.dp
    val offset = remember(item.plant.id) { Animatable(0f) }
    val openOffsetPx = with(density) { openOffset.toPx() }
    val editThresholdPx = with(density) { actionSize.toPx() }

    fun closeAndSaveActions() {
        coroutineScope.launch {
            offset.animateTo(0f)
            if (shouldWater) onWater()
            shouldWater = false
            actionSwipeOpen = false
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        WaterActionTile(
            selected = shouldWater,
            onClick = { shouldWater = !shouldWater },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(actionSize),
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            val minimumOffset = if (actionSwipeOpen) 0f else -editThresholdPx
                            offset.snapTo((offset.value + delta).coerceIn(minimumOffset, openOffsetPx))
                        }
                    },
                    onDragStopped = {
                        coroutineScope.launch {
                            when {
                                actionSwipeOpen && offset.value < openOffsetPx * 0.65f -> closeAndSaveActions()
                                actionSwipeOpen -> offset.animateTo(openOffsetPx)
                                offset.value > openOffsetPx * 0.35f -> {
                                    actionSwipeOpen = true
                                    offset.animateTo(openOffsetPx)
                                }
                                offset.value < -editThresholdPx * 0.6f -> {
                                    offset.animateTo(0f)
                                    onEdit()
                                }
                                else -> offset.animateTo(0f)
                            }
                        }
                    },
                ),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .combinedClickable(
                        onClick = onShowDetails,
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

@Composable
private fun PlantDetailsDialog(
    item: PlantListItem,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
) {
    val plant = item.plant
    val wateringSummary = item.wateringSummary

    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Image placeholder",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Text(
                    text = "Common name",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                if (plant.scientificName.isNotBlank()) {
                    Text(
                        text = "Scientific name",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                    Text(
                        text = plant.scientificName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Text(
                    text = "Watering details",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = wateringSummary.detailsText(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onEdit) { Text("Edit") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
    )
}

private fun PlantWateringSummary.detailsText(): String {
    val wateringText = when (wateringCount) {
        0 -> "No waterings yet"
        1 -> "Watered 1 time"
        else -> "Watered $wateringCount times"
    }

    return if (lastWateredAt == null) {
        wateringText
    } else {
        "$wateringText\nLast watered $lastWateredAt"
    }
}

@Composable
private fun WaterActionTile(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (selected) Color(0xFF2196F3) else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.WaterDrop,
            contentDescription = "Water plant",
            tint = contentColor,
            modifier = Modifier.size(32.dp),
        )
    }
}
