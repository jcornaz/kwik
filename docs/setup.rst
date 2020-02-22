Setup
=====

Make sure to setup a test engine
--------------------------------

Kwik is not a test-engine, but only an assertion library.

So before being able to use Kwik you have to setup a test-engine for your project.
If the project is for the JVM (Java), you probably want to use Junit_ or Spek_.

.. note::
    If you choose to use kotest_ as a test-engine, be aware that it includes a similar property-based testing API.

    In order to not get confused by mixing the two libraries, you may exclude the ``kotlintest-assertions`` artifact
    or introduce some rules in your IDE/linter to prevent usages of the package ``io.kotest.property``.

.. _Junit: https://junit.org/junit5
.. _Spek: https://spekframework.org
.. _kotest: https://github.com/kotest/kotest

Add the required repository to your build system
------------------------------------------------

- Stable versions are published on jcenter_
- Alpha, beta and release-candidates are published on https://dl.bintray.com/kwik/preview

.. _jcenter: https://bintray.com/bintray/jcenter

Add the artifact dependency
---------------------------

- The group id is ``com.github.jcornaz.kwik``
- Pick the artifact id that suits your platform:

  * ``kwik-core-common``
  * ``kwik-core-jvm``
  * ``kwik-core-linux``
  * ``kwik-core-windows``

- Pick a version from: https://github.com/jcornaz/kwik/releases

Example with gradle for Kotlin/JVM
..................................

.. include:: ../README.rst
    :start-after: .. startGradleSetup
    :end-before: .. endGradleSetup

Kotlin/JVM configuration
------------------------

If you compile Kotlin to Java ByteCode, you must target Java 8 or above.

Here is how to configure it with gradle

.. literalinclude:: ../example/build.gradle.kts
    :language: kotlin
    :start-after: //region Configure Kotlin JVM compilation
    :end-before: //endregion
