kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":karbonpowered-network"))
                api(project(":karbonpowered-protocol"))
            }
        }
    }
}