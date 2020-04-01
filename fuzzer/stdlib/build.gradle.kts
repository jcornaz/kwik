kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":fuzzer-api"))
                implementation(project(":generator-stdlib"))
            }
        }
    }
}
