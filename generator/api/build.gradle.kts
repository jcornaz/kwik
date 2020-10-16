kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(project(":generator-test"))
                api(project(":api"))
            }
        }
    }
}
