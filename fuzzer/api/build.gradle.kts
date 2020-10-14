kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generator-api"))
                api(project(":base"))
            }
        }
    }
}
