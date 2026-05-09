package org.michael.plantapp.model

data class KnownPlant(
    val commonName: String,
    val scientificName: String,
)

val SampleKnownPlants = listOf(
    KnownPlant(commonName = "Monstera", scientificName = "Monstera deliciosa"),
    KnownPlant(commonName = "Fiddle-leaf fig", scientificName = "Ficus lyrata"),
    KnownPlant(commonName = "Snake plant", scientificName = "Dracaena trifasciata"),
    KnownPlant(commonName = "Peace lily", scientificName = "Spathiphyllum wallisii"),
    KnownPlant(commonName = "Pothos", scientificName = "Epipremnum aureum"),
    KnownPlant(commonName = "ZZ plant", scientificName = "Zamioculcas zamiifolia"),
    KnownPlant(commonName = "Rubber plant", scientificName = "Ficus elastica"),
    KnownPlant(commonName = "Aloe vera", scientificName = "Aloe vera"),
    KnownPlant(commonName = "Spider plant", scientificName = "Chlorophytum comosum"),
    KnownPlant(commonName = "Chinese money plant", scientificName = "Pilea peperomioides"),
)
