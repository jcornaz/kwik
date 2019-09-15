kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generator-api"))
                implementation(project(":random"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":generator-test"))
            }
        }
    }
}
