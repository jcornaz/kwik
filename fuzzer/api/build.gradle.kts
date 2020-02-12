kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generator-api"))
            }
        }
    }
}
