@file:Suppress("UNUSED_VARIABLE")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import kr.motd.gradle.sphinx.gradle.SphinxTask
import org.codehaus.plexus.util.Os
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import java.util.*

plugins {
    `maven-publish`
    kotlin("multiplatform") version "1.3.61"
    id("org.ajoberstar.reckon") version "0.12.0"
    id("com.github.ben-manes.versions") version "0.27.0"
    id("io.gitlab.arturbosch.detekt") version "1.5.0"
    id("com.jfrog.bintray") version "1.8.4" apply false
    id("kr.motd.sphinx") version "2.6.1"
}

reckon {
    scopeFromProp()
    stageFromProp("alpha", "beta", "rc", "final")
}

detekt {
    input = files(
        subprojects.flatMap { project ->
            listOf(
                "${project.projectDir}/src/commonMain/kotlin",
                "${project.projectDir}/src/jvmMain/kotlin"
            )
        }
    )
    buildUponDefaultConfig = true
    config = files("$rootDir/detekt-config.yml")
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
    apply<BintrayPlugin>()
    apply<MavenPublishPlugin>()

    kotlin {
        jvm()
        linuxX64("linux")
        mingwX64("windows")

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

    publishing {
        publications.withType<MavenPublication>().apply {
            val metadata by getting {
                artifactId = "kwik-${project.name}-common"
            }

            val jvm by getting {
                artifactId = "kwik-${project.name}-jvm"
            }

            if (Os.isFamily(Os.FAMILY_UNIX)) {
                val linux by getting {
                    artifactId = "kwik-${project.name}-linux"
                }
            }

            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                val windows by getting {
                    artifactId = "kwik-${project.name}-windows"
                }
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
                '-' in project.version.toString() -> "preview"
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

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            setPublications("windows")
        } else {
            setPublications("metadata", "jvm", "linux")
        }
    }

    tasks {

        withType<KotlinJvmCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        val bintrayUpload by existing {
            dependsOn("check")

            if ("test" in project.name || "examples" in project.name)
                enabled = false
        }
    }
}

tasks {
    register("version") {
        doLast {
            println(project.version)
        }
    }

    val sphinx by existing {
        inputs.file("$rootDir/CHANGELOG.rst")
        inputs.file("$rootDir/README.rst")
        outputs.cacheIf { true }
    }

    withType<SphinxTask> {
        warningsAsErrors = true

        setSourceDirectory("$rootDir/docs")
    }

    val check by existing {
        dependsOn(sphinx)
    }
}
