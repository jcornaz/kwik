rootProject.name = "kwik"

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
    }
}

include("core")

include("generator-api")
include("generator-stdlib")
include("generator-test")

include("fuzzer-api")
include("fuzzer-stdlib")

include("evaluator")

project(":generator-api").projectDir = file("generator/api")
project(":generator-stdlib").projectDir = file("generator/stdlib")
project(":generator-test").projectDir = file("generator/test")

project(":fuzzer-api").projectDir = file("fuzzer/api")
project(":fuzzer-stdlib").projectDir = file("fuzzer/stdlib")
