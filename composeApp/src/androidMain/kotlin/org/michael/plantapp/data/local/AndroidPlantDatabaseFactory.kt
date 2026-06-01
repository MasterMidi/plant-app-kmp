package org.michael.plantapp.data.local

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.michael.plantapp.db.PlantDatabase

class AndroidPlantDatabaseFactory(
    private val context: Context,
) : PlantDatabaseFactory {
    override fun createDatabase(): PlantDatabase {
        val driver = AndroidSqliteDriver(
            schema = PlantDatabase.Schema,
            context = context,
            name = "plant.db",
        )
        return PlantDatabase(driver)
    }
}
