kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generators-api"))
            }
        }
    }
}
