kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(project(":generator-test"))
            }
        }
    }
}
