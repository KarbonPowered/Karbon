kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":karbonpowered-common"))
                api(project(":karbonpowered-minecraft-text"))
            }
        }
    }
}