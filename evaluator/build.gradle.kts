kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generator-api"))
                implementation(project(":generator-stdlib"))
            }
        }
    }
}
