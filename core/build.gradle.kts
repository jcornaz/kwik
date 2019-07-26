kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":assertions"))
                api(project(":generator-api"))
                api(project(":generator-stdlib"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":generator-test"))
            }
        }
    }
}
