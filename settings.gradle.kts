rootProject.name = "kwik"

include("core")

include("random")

include("generator-api")
include("generator-stdlib")
include("generator-test")

include("evaluator")

project(":generator-api").projectDir = file("generator/api")
project(":generator-stdlib").projectDir = file("generator/stdlib")
project(":generator-test").projectDir = file("generator/test")
