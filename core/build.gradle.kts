kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":evaluator"))
                api(project(":random"))
                api(project(":generator-api"))
                api(project(":generator-stdlib"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":generator-test"))
            }
        }
    }
}
