kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":fuzzer-api"))
            }
        }
    }
}
