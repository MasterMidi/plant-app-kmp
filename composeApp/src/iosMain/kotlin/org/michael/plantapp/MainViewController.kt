package org.michael.plantapp

import androidx.compose.ui.window.ComposeUIViewController
import org.michael.plantapp.data.local.IosPlantDatabaseFactory

fun MainViewController() = ComposeUIViewController {
    App(
        databaseFactory = IosPlantDatabaseFactory(),
    )
}
