kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generator-api"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":generator-test"))
            }
        }
    }
}
