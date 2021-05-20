kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":karbonpowered-protocol-java-edition"))
                api(project(":karbonpowered-math"))
            }
        }
    }
}