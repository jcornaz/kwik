rootProject.name = "kwik"

enableFeaturePreview("GRADLE_METADATA")

include("generator-api")
include("generator-stdlib")
include("generator-test")

include("evaluator")
include("fuzzer-api")

project(":generator-api").projectDir = file("generator/api")
project(":generator-stdlib").projectDir = file("generator/stdlib")
project(":generator-test").projectDir = file("generator/test")

project(":fuzzer-api").projectDir = file("fuzzer/api")
