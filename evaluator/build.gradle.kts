kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generator-api"))
                api(project(":fuzzer-api"))
                implementation(project(":generator-stdlib"))
            }
        }
    }
}
