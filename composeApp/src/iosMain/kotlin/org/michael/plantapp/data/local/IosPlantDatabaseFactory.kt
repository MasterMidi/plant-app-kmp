package org.michael.plantapp.data.local

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.michael.plantapp.db.PlantDatabase

class IosPlantDatabaseFactory : PlantDatabaseFactory {
    override fun createDatabase(): PlantDatabase {
        val driver = NativeSqliteDriver(
            schema = PlantDatabase.Schema,
            name = "plant.db",
        )
        return PlantDatabase(driver)
    }
}