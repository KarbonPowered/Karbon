kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":karbonpowered-common"))
                implementation("io.ktor:ktor-network:1.5.1")
            }
        }
    }
}