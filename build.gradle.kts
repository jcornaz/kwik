@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform") version "1.3.31"
    id("org.ajoberstar.reckon") version "0.10.0"
    id("com.github.ben-manes.versions") version "0.21.0"
}

group = "com.github.jcornaz.kwik"

reckon {
    scopeFromProp()
    stageFromProp("alpha", "beta", "rc", "final")
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")

    kotlin {
        jvm()

        sourceSets {
            commonMain {
                dependencies {
                    api(kotlin("stdlib-common"))
                }
            }

            commonTest {
                dependencies {
                    api(kotlin("test-common"))
                    api(kotlin("test-annotations-common"))
                }
            }

            val jvmMain by existing {
                dependencies {
                    implementation(kotlin("stdlib"))
                }
            }

            val jvmTest by existing {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }
}

tasks.register("version") {
    doLast {
        println(project.version)
    }
}
