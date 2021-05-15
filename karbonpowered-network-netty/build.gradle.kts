kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                val nettyVersion: String by project
                val nettyIoUringVersion: String by project
                implementation("io.netty:netty-transport:$nettyVersion")
                implementation("io.netty:netty-codec:$nettyVersion")
                implementation("io.netty:netty-transport-native-epoll:$nettyVersion")
                if (org.gradle.internal.os.OperatingSystem.current().isLinux) {
                    implementation("io.netty:netty-incubator-transport-native-io_uring:0.0.5.Final")
                }
                implementation(project(":karbonpowered-network"))
            }
        }
    }
}