rootProject.name = "KarbonPowered"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

include("karbonpowered-common")
include("karbonpowered-network")
include("karbonpowered-network-netty")
include("karbonpowered-server")
include("karbonpowered-api")
include("karbonpowered-text")
include("karbonpowered-audience")
include("karbonpowered-protocol")
//include("karbonpowered-engine")
include("karbonpowered-nbt")
include("karbonpowered-io")
include("karbonpowered-data")
include("karbonpowered-profile")
include("karbonpowered-math")
include("karbonpowered-core")
