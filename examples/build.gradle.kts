kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":evaluator"))
                api(project(":generator-stdlib"))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        jvmMain {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}
