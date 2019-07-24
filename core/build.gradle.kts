kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":assertions"))
                api(project(":generators-api"))
                api(project(":generators-stdlib"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":generators-test"))
            }
        }
    }
}
