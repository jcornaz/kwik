@file:Suppress("UNUSED_VARIABLE")

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.detekt
import org.sonarqube.gradle.SonarQubeTask

plugins {
    kotlin("multiplatform") version "1.3.31"
    id("org.ajoberstar.reckon") version "0.10.0"
    id("com.github.ben-manes.versions") version "0.21.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14" apply false
    id("org.sonarqube") version "2.7"
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
    apply<DetektPlugin>()

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

    detekt {
        input = files(
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/jvmMain/kotlin",
            "src/jvmTest/kotlin"
        )
        buildUponDefaultConfig = true
        config = files("$rootDir/detekt-config.yml")
    }

    subprojects {
        sonarqube {
            properties {
                property("sonar.kotlin.detekt.reportPaths", "build/reports/detekt/detekt.xml")
                property(
                    "sonar.sources", listOf(
                        "src/commonMain/kotlin",
                        "src/commonTest/kotlin",
                        "src/jvmMain/kotlin",
                        "src/jvmTest/kotlin"
                    )
                )
            }
        }
    }
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "jcornaz-github")
        property("sonar.projectKey", "jcornaz_kwik")

        property("sonar.projectVersion", project.version.toString().let {
            if ('+' in it) it.substringBeforeLast('.') else it
        })

        property("sonar.coverage.exclusions", "**/commonMain/**")
    }
}

tasks {
    register("version") {
        doLast {
            println(project.version)
        }
    }

    withType<SonarQubeTask> {
        subprojects.forEach {
            dependsOn("${it.path}:detekt")
        }
    }
}
