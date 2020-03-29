import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    kotlin("jvm") version "1.3.61"
}

group = "com.github.jcornaz.kwik"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")

    testImplementation("com.github.jcornaz.kwik:kwik-evaluator-jvm:+")
    testImplementation("com.github.jcornaz.kwik:kwik-generator-stdlib-jvm:+")
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
