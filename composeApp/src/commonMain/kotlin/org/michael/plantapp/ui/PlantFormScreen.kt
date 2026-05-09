package org.michael.plantapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.michael.plantapp.model.Plant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantFormScreen(
    existing: Plant? = null,
    initialName: String = existing?.name ?: "",
    initialScientificName: String = existing?.scientificName ?: "",
    initialKnownPlantId: String? = existing?.knownPlantId,
    onSave: (name: String, scientificName: String, knownPlantId: String?) -> Unit,
    onCancel: () -> Unit,
    onSearchKnownPlant: (name: String, scientificName: String, knownPlantId: String?) -> Unit,
) {
    var name by remember(existing?.id, initialName) { mutableStateOf(initialName) }
    var scientificName by remember(existing?.id, initialScientificName) { mutableStateOf(initialScientificName) }
    var knownPlantId by remember(existing?.id, initialKnownPlantId) {
        mutableStateOf(initialKnownPlantId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existing == null) "Add plant" else "Edit plant") },
                actions = {
                    TextButton(onClick = onCancel) { Text("Cancel") }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        onClick = { onSearchKnownPlant(name, scientificName, knownPlantId) },
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
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { onSave(name, scientificName, knownPlantId) },
                    enabled = name.isNotBlank(),
                ) {
                    Text(if (existing == null) "Add" else "Save")
                }
            }
        }
    }
}
