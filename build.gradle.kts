@file:Suppress("UNUSED_VARIABLE")

import kr.motd.gradle.sphinx.gradle.SphinxTask
import org.codehaus.plexus.util.Os
import org.gradle.api.publish.maven.MavenPublication

plugins {
    `maven-publish`
    signing
    id("org.jetbrains.kotlin.multiplatform") version "1.4.30"
    id("com.github.ben-manes.versions") version "0.36.0"
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("com.jfrog.bintray") version "1.8.5" apply false
    id("kr.motd.sphinx") version "2.9.0"
    id("io.codearte.nexus-staging") version "0.22.0"
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

val ossrhUser get() = System.getenv("SONATYPE_USER_NAME")
val ossrhPassword get() = System.getenv("SONATYPE_PASSWORD")

nexusStaging {
    username = ossrhUser
    password = ossrhPassword
    packageGroup = "com.github.jcornaz"
}

// Hack so that we can configure all subprojects from this file
kotlin { jvm() }

subprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply<MavenPublishPlugin>()
    apply<JacocoPlugin>()
    apply<JavaPlugin>()
    apply<SigningPlugin>()

    configure<SigningExtension> {
        sign(configurations.archives.get())
    }

    kotlin {
        jvm {
            compilations.all {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
        }
        linuxX64("linux")
        mingwX64("windows")

        @Suppress("SuspiciousCollectionReassignment")
        targets.all {
            compilations.all {
                explicitApi()
                kotlinOptions {
                    allWarningsAsErrors = findProperty("warningAsError") != null
                    freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
                }
            }
        }

        sourceSets {
            commonTest {
                dependencies {
                    api(kotlin("test-common"))
                    api(kotlin("test-annotations-common"))
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

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.6"
    }

    publishing {
        repositories {
            maven {
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = ossrhUser
                    password = ossrhUser
                }
            }
        }
        publications.withType<MavenPublication>().apply {
            fun MavenPublication.config() {
                pom {
                    name.set("Kwik")
                    description.set("Property-based testing library for Kotlin")
                    url.set("https://github.com/jcornaz/kwik")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("jcornaz")
                            name.set("Jonathan Cornaz")
                            email.set("jonathan.cornaz@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git@github.com:jcornaz/kwik.git")
                        url.set("https://github.com/jcornaz/kwik")
                    }
                }
            }

            val metadata by getting {
                config()
                artifactId = "kwik-${project.name}-common"
            }

            val jvm by getting {
                config()
                artifactId = "kwik-${project.name}-jvm"
            }

            if (Os.isFamily(Os.FAMILY_UNIX)) {
                val linux by getting {
                    config()
                    artifactId = "kwik-${project.name}-linux"
                }
            }

            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                val windows by getting {
                    config()
                    artifactId = "kwik-${project.name}-windows"
                }
            }
        }
    }

    tasks {
        val jvmTest by existing {
            finalizedBy("jacocoTestReport")
        }

        val check by existing {
            dependsOn("publishToMavenLocal")
        }

        val jacocoTestReport by existing(JacocoReport::class) {
            dependsOn(jvmTest)

            classDirectories.setFrom(File("$buildDir/classes/kotlin/jvm/main").walkBottomUp().toSet())
            sourceDirectories.setFrom(files("src/commonMain/kotlin", "src/jvmMain/kotlin"))
            executionData.setFrom(files("${buildDir}/jacoco/jvmTest.exec"))

            reports {
                xml.isEnabled = true
                html.isEnabled = true
            }
        }
    }
}

tasks {
    val version by registering {
        group = "Help"
        description = "Prints version of kwik"

        doLast {
            println(project.version)
        }
    }

    val detekt by existing {
        description = "Performs static code analysis and report detected code smells"
    }

    val sphinx by existing(SphinxTask::class) {
        setWarningsAsErrors(true)

        setSourceDirectory("$rootDir/docs")
        inputs.file("$rootDir/README.rst")
        inputs.dir("$rootDir/example")

        outputs.cacheIf { true }
    }

    val testReport by registering(TestReport::class) {
        group = "Verification"
        description = "Create an aggregated report of all test tasks"

        destinationDir = file("$buildDir/reports/allTests")
        reportOn(subprojects.flatMap { it.tasks.withType(Test::class) })

        dependsOn.clear()
        mustRunAfter(subprojects.flatMap { it.tasks.withType(Test::class) })
    }

    val check by existing {
        dependsOn(sphinx)
    }
}
