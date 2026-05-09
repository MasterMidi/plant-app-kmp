package org.michael.plantapp.model

data class KnownPlant(
    val id: String,
    val commonName: String,
    val scientificName: String,
)

val SampleKnownPlants = listOf(
    KnownPlant(
        id = "0196b2a8-0001-7000-8000-000000000001",
        commonName = "Monstera",
        scientificName = "Monstera deliciosa",
    ),
    KnownPlant(
        id = "0196b2a8-0002-7000-8000-000000000002",
        commonName = "Fiddle-leaf fig",
        scientificName = "Ficus lyrata",
    ),
    KnownPlant(
        id = "0196b2a8-0003-7000-8000-000000000003",
        commonName = "Snake plant",
        scientificName = "Dracaena trifasciata",
    ),
    KnownPlant(
        id = "0196b2a8-0004-7000-8000-000000000004",
        commonName = "Peace lily",
        scientificName = "Spathiphyllum wallisii",
    ),
    KnownPlant(
        id = "0196b2a8-0005-7000-8000-000000000005",
        commonName = "Pothos",
        scientificName = "Epipremnum aureum",
    ),
    KnownPlant(
        id = "0196b2a8-0006-7000-8000-000000000006",
        commonName = "ZZ plant",
        scientificName = "Zamioculcas zamiifolia",
    ),
    KnownPlant(
        id = "0196b2a8-0007-7000-8000-000000000007",
        commonName = "Rubber plant",
        scientificName = "Ficus elastica",
    ),
    KnownPlant(
        id = "0196b2a8-0008-7000-8000-000000000008",
        commonName = "Aloe vera",
        scientificName = "Aloe vera",
    ),
    KnownPlant(
        id = "0196b2a8-0009-7000-8000-000000000009",
        commonName = "Spider plant",
        scientificName = "Chlorophytum comosum",
    ),
    KnownPlant(
        id = "0196b2a8-000a-7000-8000-00000000000a",
        commonName = "Chinese money plant",
        scientificName = "Pilea peperomioides",
    ),
)
