kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generators-api"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":generators-test"))
            }
        }
    }
}
