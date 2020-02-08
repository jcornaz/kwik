Setup
=====

Make sure to setup a test engine
--------------------------------

Kwik is not a test-engine, but only an assertion library.

So before being able to use Kwik you have to setup a test-engine for your project.
If the project is for the JVM (Java), you probably want to use Junit_ or Spek_.

.. note::
    If you choose to use kotlintest_ as a test-engine, be aware that it includes a similar property-based testing API.

    In order to not get confused by mixing the two libraries, you may exclude the ``kotlintest-assertions`` artifact
    or introduce some rules in your IDE/linter to prevent usages of the package `io.kotlintest.properties`.

.. _Junit: https://junit.org/junit5
.. _Spek: https://spekframework.org
.. _kotlintest: https://github.com/kotlintest/kotlintest

Add the required repository to your build system
------------------------------------------------

- Stable versions are published on jcenter_
- Alpha, beta and release-candidates are published on https://dl.bintray.com/kwik/preview

.. _jcenter: https://bintray.com/bintray/jcenter

Add the artifact dependency
---------------------------

- The group id is ``com.github.jcornaz.kwik``
- Artifact ids have the form of ``kwik-<MODULE>-<PLATFORM>``

    - Available modules:

        - ``evaluator``: property evaluation
        - ``generator-api``: API for random genetor, already transitively added via ``evaluator`` or ``generator-stdlib``
        - ``generator-stdlib``: Collection of random generators for types provided by the kotlin standard library

    - Available platforms: ``jvm``, ``linux`` and ``windows``

    - Example: ``kwik-evaluator-jvm``

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
