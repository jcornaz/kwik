kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":fuzzer-api"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":generator-stdlib"))
            }
        }
    }
}
