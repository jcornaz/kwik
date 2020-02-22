kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":evaluator"))
                api(project(":fuzzer-api"))
                api(project(":generator-stdlib"))
            }
        }
    }
}
