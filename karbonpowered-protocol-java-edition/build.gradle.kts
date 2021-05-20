kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":karbonpowered-common"))
                api(project(":karbonpowered-api"))
                api(project(":karbonpowered-core"))
                api(project(":karbonpowered-network"))
                api(project(":karbonpowered-nbt"))
            }
        }
    }
}