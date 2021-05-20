kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":karbonpowered-network-netty"))
                api(project(":karbonpowered-protocol-java-edition"))
            }
        }
    }
}