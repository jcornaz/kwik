@file:Suppress("UNUSED_VARIABLE")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.detekt
import org.sonarqube.gradle.SonarQubeTask
import java.util.*

plugins {
    `maven-publish`
    kotlin("multiplatform") version "1.3.31"
    id("org.ajoberstar.reckon") version "0.10.0"
    id("com.github.ben-manes.versions") version "0.21.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14" apply false
    id("org.sonarqube") version "2.7"
    id("com.jfrog.bintray") version "1.8.4" apply false
}

reckon {
    scopeFromProp()
    stageFromProp("alpha", "beta", "rc", "final")
}

allprojects {
    group = "com.github.jcornaz.kwik"

    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply<DetektPlugin>()
    apply<BintrayPlugin>()
    apply<MavenPublishPlugin>()

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
            "src/jvmMain/kotlin",
            "src/commonTest/kotlin",
            "src/jvmTest/kotlin"
        )
        buildUponDefaultConfig = true
        config = files("$rootDir/detekt-config.yml")
    }

    publishing {
        publications.withType<MavenPublication>().apply {
            val metadata by getting {
                artifactId = "kwik-${project.name}-common"
            }

            val jvm by getting {
                artifactId = "kwik-${project.name}-jvm"
            }
        }
    }

    configure<BintrayExtension> {
        user = System.getenv("BINTRAY_USER")
        key = System.getenv("BINTRAY_KEY")
        publish = true

        with(pkg) {
            userOrg = "kwik"
            name = "kwik"
            repo = when {
                '+' in project.version.toString() -> "dev"
                '-' in project.version.toString() -> "beta"
                else -> "stable"
            }

            setLicenses("Apache-2.0")

            vcsUrl = "https://github.com/jcornaz/kwik"
            githubRepo = "jcornaz/kwik"

            with(version) {
                name = project.version.toString()
                released = Date().toString()
                if ('+' !in project.version.toString()) {
                    vcsTag = project.version.toString()
                }
            }
        }

        setPublications("metadata", "jvm")
    }

    tasks.named("bintrayUpload") {
        dependsOn("check")
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

        property("sonar.kotlin.detekt.reportPaths", "build/reports/detekt/detekt.xml")
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
