rootProject.name = "Karbon"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

include("karbonpowered-common")
//include("karbonpowered-network")
include("karbonpowered-api")
include("karbonpowered-text")
//include("karbonpowered-protocol")re
//include("karbonpowered-engine")
include("karbonpowered-nbt")
include("karbonpowered-io")
include("karbonpowered-data")
include("karbonpowered-profile")
include("karbonpowered-math")
//include("karbonpowered-physics")
