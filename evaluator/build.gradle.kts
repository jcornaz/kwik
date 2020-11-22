kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":api"))
                api(project(":fuzzer-api"))
                implementation(project(":generator-stdlib"))
            }
        }
    }
}
