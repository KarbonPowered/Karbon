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
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap/")
    }

    kotlin {
        jvm {
            withJava()
        }
        sourceSets {
            val commonMain by getting {
                dependencies {
                    val ktorVersion: String by project
                    api("org.jetbrains.kotlinx:atomicfu:0.16.2")
                    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
                    api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.4")
                    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
                    api("io.ktor:ktor-io:$ktorVersion")
                    api("io.ktor:ktor-network:$ktorVersion")
                }
            }
            commonTest {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                }
            }
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }

    val compilerArgs = listOf(
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xopt-in=kotlin.ExperimentalUnsignedTypes",
        "-Xopt-in=kotlin.time.ExperimentalTime",
    )

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
            apiVersion = "1.5"
            languageVersion = "1.5"
            freeCompilerArgs = freeCompilerArgs + compilerArgs
//            allWarningsAsErrors = true
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon> {
        kotlinOptions {
            apiVersion = "1.5"
            languageVersion = "1.5"
            freeCompilerArgs = freeCompilerArgs + compilerArgs
//            allWarningsAsErrors = true
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

subprojects {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = this@subprojects.group.toString()
                artifactId = this@subprojects.name
                version = this@subprojects.version.toString()
                from(components["java"])
            }
        }

        repositories {
            if (System.getenv("GITHUB_TOKEN") != null) {
                maven {
                    name = "GitHubPackages"
                    setUrl("https://maven.pkg.github.com/KarbonPowered/Karbon")
                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }
}

application {
    mainClass.set("com.karbonpowered.engine.MainKt")
}
