kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":karbonpowered-text"))
                implementation(project(":karbonpowered-nbt"))
                implementation(project(":karbonpowered-data"))
                implementation(project(":karbonpowered-api"))
            }
        }
    }
}