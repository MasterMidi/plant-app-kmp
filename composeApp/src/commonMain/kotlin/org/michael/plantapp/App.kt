package org.michael.plantapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import org.michael.plantapp.ui.PlantListScreen
import org.michael.plantapp.viewmodel.PlantListViewModel

@Composable
fun App() {
    MaterialTheme {
        val viewModel = viewModel { PlantListViewModel() }
        PlantListScreen(viewModel)
    }
}