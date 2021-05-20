plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    application
    `maven-publish`
}

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "application")
    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")

    group = "com.karbonpowered"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    kotlin {
        jvm {
            withJava()
        }
        sourceSets {
            val commonMain by getting {
                dependencies {
                    api("org.jetbrains.kotlinx:atomicfu:0.16.1")
                    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC")
                    api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.4")
                    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
                    api("io.ktor:ktor-io:1.5.4")
                    api("io.ktor:ktor-network:1.5.4")
                }
            }
        }
    }

    val compilerArgs = listOf(
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xopt-in=kotlin.ExperimentalUnsignedTypes",
        "-Xopt-in=kotlin.time.ExperimentalTime"
    )

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
            freeCompilerArgs = compilerArgs
            allWarningsAsErrors = true
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon> {
        kotlinOptions {
            freeCompilerArgs = compilerArgs
            allWarningsAsErrors = true
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = this@allprojects.group.toString()
                artifactId = this@allprojects.name
                version = this@allprojects.version.toString()
            }
        }
    }
}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                subprojects {
                    api(this)
                }
            }
        }
    }
}

application {
    mainClass.set("com.karbonpowered.engine.MainKt")
}