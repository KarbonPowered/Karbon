rootProject.name = "Karbon"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    plugins {
        val kotlinVersion: String by settings

        kotlin("multiplatform") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

include("karbonpowered-common")
include("karbonpowered-network")
include("karbonpowered-network-netty")
include("karbonpowered-api")
include("karbonpowered-text")
include("karbonpowered-audience")
include("karbonpowered-protocol-java-edition")
include("karbonpowered-engine")
include("karbonpowered-nbt")
include("karbonpowered-io")
include("karbonpowered-data")
include("karbonpowered-profile")
include("karbonpowered-math")
include("karbonpowered-core")
include("karbonpowered-vanilla")
