rootProject.name = "KarbonPowered"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include("karbonpowered-common")
include("karbonpowered-network")
include("karbonpowered-api")
include("karbonpowered-text")
include("karbonpowered-protocol")
include("karbonpowered-engine")