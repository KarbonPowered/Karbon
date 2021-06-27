kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":karbonpowered-common"))
                implementation(project(":karbonpowered-core"))
                implementation(project(":karbonpowered-data"))
                implementation(project(":karbonpowered-math"))
                implementation(project(":karbonpowered-network"))
            }
        }
    }
}
