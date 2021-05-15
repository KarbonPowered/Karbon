kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":karbonpowered-protocol"))
                implementation(project(":karbonpowered-text"))
                implementation(project(":karbonpowered-nbt"))
                implementation(project(":karbonpowered-data"))
                implementation(project(":karbonpowered-api"))
            }
        }
    }
}