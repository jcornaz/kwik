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


.. _configure-default-iterations:

Configure the default number of iterations
------------------------------------------

By default Kwik will evaluate each property 200 times. (each time with different random inputs)

This default can be configured by defining the system property ``kwik.iterations`` (on JVM)
or environment variable ``KWIK_ITERATIONS`` (on JVM and Linux).
If both are defined (on JVM), then the system property has precedence.

It can be especially useful to define a different number of iteration on the CI server

For instance one may write the following gradle setup:

.. code-block:: Kotlin

    tasks.withType<Test> {
        if ("CI" in System.getenv()) {

            // On the CI take more time to try falsifying each property
            systemProperty("kwik.iterations", "10000")
        } else {

            // On the local setup allow the developer specify by command line using `-Dkwik.iterations=`
            systemProperty("kwik.iterations", System.getProperty("kwik.iterations"))
        }
    }

With the setup above each property would be evaluated 10'000 times (with different random inputs) when test are executed
on the CI server. (to make it work, the server needs to have a ``CI`` environment variable)

And any developer may run ``./gradlew test -Dkwik.properties=10`` if he wants a fast feedback loop,
evaluating each property only 10 times.

.. note::
    The number of iteration defined when invoking ``forAll`` has precedence over the system property.

    See :ref:`how to choose number of iterations for specific property <choose-property-iterations>`
