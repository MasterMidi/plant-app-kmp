package org.michael.plantapp.data.local

import org.michael.plantapp.db.PlantDatabase

interface PlantDatabaseFactory {
    fun createDatabase(): PlantDatabase
}