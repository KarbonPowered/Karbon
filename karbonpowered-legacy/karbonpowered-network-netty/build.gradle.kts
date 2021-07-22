kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                val nettyVersion: String by project
                implementation("io.netty:netty-transport:$nettyVersion")
                implementation("io.netty:netty-codec:$nettyVersion")
                implementation("io.netty:netty-transport-native-epoll:$nettyVersion")
                implementation(project(":karbonpowered-network"))
            }
        }
    }
}