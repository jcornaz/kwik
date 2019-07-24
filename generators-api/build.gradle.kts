kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(project(":generators-test"))
            }
        }
    }
}
