kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":generators-api"))
                implementation(project(":generators-stdlib"))
            }
        }
    }
}
