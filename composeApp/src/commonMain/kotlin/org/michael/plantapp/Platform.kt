package org.michael.plantapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform