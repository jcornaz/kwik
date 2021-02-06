import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    kotlin("jvm") version "1.4.30"
}

group = "com.github.jcornaz.kwik"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")

    testImplementation("com.github.jcornaz.kwik:kwik-core-jvm:+")
}

//region Configure Kotlin JVM compilation
tasks.withType<KotlinJvmCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
//endregion

tasks.test {
    useJUnitPlatform()
}
