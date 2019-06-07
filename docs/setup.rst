Setup
=====

**1. Make sure to setup a test engine**

Kwik is not a test-engine, but only an assertion library.

So before being able to use Kwik you have to setup a test-engine for your project.
If the project is for the JVM (Java), you probably want to use Junit_ or Spek_.

.. note::
    kotlintest_ provide a similar API than Kwik.

    In order to don't get confused by mixing the two libraries you may exclude the ``kotlintest-assertions`` artifact
    or introduce some rules in your IDE and/or linter to prevent usages of the package `io.kotlintest.properties`.

.. _Junit: https://junit.org/junit5
.. _Spek: https://spekframework.org
.. _kotlintest: https://github.com/kotlintest/kotlintest

**2. Add the required repository to your build system:**

- Stable versions will published on jcenter_
- Alpha, beta and release-candidates are published on https://dl.bintray.com/kwik/preview
- Development artifacts are published on https://dl.bintray.com/kwik/dev

.. _jcenter: https://bintray.com/bintray/jcenter

**3. Add the artifact dependency:**

- The group id is ``com.github.jcornaz.kwik``
- The available artifact ids are:
    - ``kwik-core-jvm`` for Kotlin/JVM modues
    - ``kwik-core-common`` for Kotlin/Common modules
- Checkout a version from: https://github.com/jcornaz/kwik/releases

**Example:**

.. code-block:: kotlin

    repositories {
        maven { url = uri("https://dl.bintray.com/kwik/preview") }
    }

    dependencies {
        testCompile("com.github.jcornaz.kwik:kwik-core-jvm:0.1.0-alpha.2")
    }
