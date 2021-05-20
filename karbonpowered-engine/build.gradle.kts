kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":karbonpowered-common"))
                implementation(project(":karbonpowered-core"))
            }
        }
    }
}