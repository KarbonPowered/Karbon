rootProject.name = "KarbonPowered"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include("karbonpowered-common")
include("karbonpowered-network")
include("karbonpowered-minecraft-api")
include("karbonpowered-minecraft-text")
include("karbonpowered-minecraft-protocol")