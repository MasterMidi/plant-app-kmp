package org.michael.plantapp.model

data class Plant(
    val id: Long,
    val name: String,
    val scientificName: String = "",
    val knownPlantScientificName: String? = null,
)
