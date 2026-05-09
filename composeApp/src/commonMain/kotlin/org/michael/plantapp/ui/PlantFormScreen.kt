package org.michael.plantapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.michael.plantapp.model.Pest
import org.michael.plantapp.model.PestId
import org.michael.plantapp.model.Plant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantFormScreen(
    existing: Plant? = null,
    initialName: String = existing?.name ?: "",
    initialScientificName: String = existing?.scientificName ?: "",
    initialKnownPlantId: String? = existing?.knownPlantId,
    initialPestIds: List<PestId> = existing?.pestIds ?: emptyList(),
    availablePests: List<Pest>,
    onCreatePest: (name: String, description: String) -> Pest,
    onSave: (name: String, scientificName: String, knownPlantId: String?, pestIds: List<PestId>) -> Unit,
    onCancel: () -> Unit,
    onSearchKnownPlant: (name: String, scientificName: String, knownPlantId: String?, pestIds: List<PestId>) -> Unit,
) {
    var name by remember(existing?.id, initialName) { mutableStateOf(initialName) }
    var scientificName by remember(existing?.id, initialScientificName) { mutableStateOf(initialScientificName) }
    var knownPlantId by remember(existing?.id, initialKnownPlantId) {
        mutableStateOf(initialKnownPlantId)
    }
    var selectedPestIds by remember(existing?.id, initialPestIds) {
        mutableStateOf(initialPestIds.toSet())
    }
    var pestMenuExpanded by remember { mutableStateOf(false) }
    var creatingPest by remember { mutableStateOf(false) }
    var detailsPest by remember { mutableStateOf<Pest?>(null) }
    val selectedPests = availablePests.filter { it.id in selectedPestIds }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (existing == null) "Add plant" else "Edit plant",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancel",
                        )
                    }
                },
                expandedHeight = 52.dp,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = scientificName,
                onValueChange = {
                    scientificName = it
                    knownPlantId = null
                },
                label = { Text("Scientific name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                leadingIcon = if (knownPlantId != null) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Linked to known plant",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                } else {
                    null
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onSearchKnownPlant(name, scientificName, knownPlantId, selectedPestIds.toList())
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search known plants",
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if (knownPlantId != null) {
                Spacer(Modifier.height(8.dp))
                Text("Linked to known plant")
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Pests",
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = "Select all active pests for this plant",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Box {
                OutlinedButton(onClick = { pestMenuExpanded = true }) {
                    Text("Select pests (${selectedPestIds.size})")
                }
                DropdownMenu(
                    expanded = pestMenuExpanded,
                    onDismissRequest = { pestMenuExpanded = false },
                ) {
                    availablePests.forEach { pest ->
                        val selected = pest.id in selectedPestIds
                        DropdownMenuItem(
                            text = { Text(pest.name) },
                            leadingIcon = {
                                Checkbox(
                                    checked = selected,
                                    onCheckedChange = null,
                                )
                            },
                            onClick = {
                                selectedPestIds = if (selected) {
                                    selectedPestIds - pest.id
                                } else {
                                    selectedPestIds + pest.id
                                }
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { creatingPest = true }) {
                Text("Create pest")
            }

            if (selectedPests.isEmpty()) {
                Text(
                    text = "No pests selected",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            } else {
                Spacer(Modifier.height(8.dp))
                selectedPests.forEach { pest ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = pest.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        TextButton(onClick = { detailsPest = pest }) {
                            Text("Details")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { onSave(name, scientificName, knownPlantId, selectedPestIds.toList()) },
                    enabled = name.isNotBlank(),
                ) {
                    Text(if (existing == null) "Add" else "Save")
                }
            }
        }
    }

    if (creatingPest) {
        PestDialog(
            title = "Create pest",
            readOnly = false,
            onDismiss = { creatingPest = false },
            onSave = { pestName, description ->
                val pest = onCreatePest(pestName, description)
                selectedPestIds = selectedPestIds + pest.id
                creatingPest = false
            },
        )
    }

    detailsPest?.let { pest ->
        PestDialog(
            title = "Pest details",
            pest = pest,
            readOnly = true,
            onDismiss = { detailsPest = null },
            onSave = { _, _ -> },
        )
    }
}

@Composable
private fun PestDialog(
    title: String,
    readOnly: Boolean,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String) -> Unit,
    pest: Pest? = null,
) {
    var name by remember(pest?.id) { mutableStateOf(pest?.name ?: "") }
    var description by remember(pest?.id) { mutableStateOf(pest?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Picture placeholder",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    readOnly = readOnly,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    readOnly = readOnly,
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            if (readOnly) {
                TextButton(onClick = onDismiss) { Text("Close") }
            } else {
                Button(
                    onClick = { onSave(name, description) },
                    enabled = name.isNotBlank(),
                ) {
                    Text("Create")
                }
            }
        },
        dismissButton = if (readOnly) {
            null
        } else {
            { TextButton(onClick = onDismiss) { Text("Cancel") } }
        },
    )
}
