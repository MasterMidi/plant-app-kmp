package org.michael.plantapp.model

typealias PestId = String

data class Pest(
    val id: PestId,
    val name: String,
    val description: String,
    val builtIn: Boolean = false,
)

object BuiltInPests {
    val all = listOf(
        Pest(
            id = "gnats",
            name = "Fungus gnats",
            description = "Small black flies that hover around damp soil. Their larvae feed in the potting mix and are usually a sign that the soil is staying wet for too long.",
            builtIn = true,
        ),
        Pest(
            id = "thrips",
            name = "Thrips",
            description = "Tiny narrow insects that scrape leaves and leave silvery scarring, black specks, distorted new growth, and damaged flowers.",
            builtIn = true,
        ),
        Pest(
            id = "aphids",
            name = "Aphids",
            description = "Soft-bodied insects that cluster on stems and new growth. They suck sap, leave sticky honeydew, and can cause curling leaves.",
            builtIn = true,
        ),
        Pest(
            id = "scales",
            name = "Scales",
            description = "Small shell-like bumps attached to stems or leaves. They feed on sap and often leave sticky residue while being easy to mistake for plant markings.",
            builtIn = true,
        ),
        Pest(
            id = "mealybugs",
            name = "Mealybugs",
            description = "White cottony pests that hide in leaf joints, stems, and roots. They feed on sap and can spread quickly if not removed early.",
            builtIn = true,
        ),
    )
}
