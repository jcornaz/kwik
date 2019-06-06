Setup
=====

1. Add the required repository to your build system
---------------------------------------------------

- Stable versions will published in jcenter.
- Alpha, beta and release-candidates are published on https://dl.bintray.com/kwik/preview
- Development artifacts are published on https://dl.bintray.com/kwik/dev

2. Add the artifact dependency
------------------------------

- The group id is `com.github.jcornaz.kwik`
- The available artifact ids are:
    - `kwik-core-jvm` for Kotlin/JVM modues
    - `kwik-core-common` for Kotlin/Common modules
- The list of available version is here: https://github.com/jcornaz/kwik/releases

Example
-------

.. code-block:: kotlin

    repositories {
        maven { url = uri("https://dl.bintray.com/kwik/preview") }
    }

    dependencies {
        testCompile("com.github.jcornaz.kwik:kwik-core-jvm:0.1.0-alpha.2")
    }
