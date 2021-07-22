kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":karbonpowered-common"))
                api(project(":karbonpowered-text"))
                api(project(":karbonpowered-audience"))
                api(project(":karbonpowered-math"))
                api(project(":karbonpowered-data"))
                api(project(":karbonpowered-profile"))
            }
        }
    }
}