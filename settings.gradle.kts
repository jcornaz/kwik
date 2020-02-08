rootProject.name = "kwik"

enableFeaturePreview("GRADLE_METADATA")

include("generator-api")
include("generator-stdlib")
include("generator-test")

include("evaluator")

project(":generator-api").projectDir = file("generator/api")
project(":generator-stdlib").projectDir = file("generator/stdlib")
project(":generator-test").projectDir = file("generator/test")
