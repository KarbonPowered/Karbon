kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":karbonpowered-common"))
                api("io.ktor:ktor-io:1.5.1")
            }
        }
    }
}