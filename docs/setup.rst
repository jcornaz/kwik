Setup
=====

Make sure to setup a test engine
--------------------------------

Kwik is not a test-engine, but only an assertion library.

So before being able to use Kwik you have to setup a test-engine for your project.
If the project is for the JVM (Java), you probably want to use Junit_ or Spek_.

.. note::
    If you choose to use kotlintest_ as a test-engine, be aware that it includes a similar property-based testing API.

    In order to don't get confused by mixing the two libraries you may exclude the ``kotlintest-assertions`` artifact
    or introduce some rules in your IDE and/or linter to prevent usages of the package `io.kotlintest.properties`.

.. _Junit: https://junit.org/junit5
.. _Spek: https://spekframework.org
.. _kotlintest: https://github.com/kotlintest/kotlintest

Add the required repository to your build system
------------------------------------------------

- Stable versions will be published on jcenter_
- Alpha, beta and release-candidates are published on https://dl.bintray.com/kwik/preview
- Development artifacts are published on https://dl.bintray.com/kwik/dev

.. _jcenter: https://bintray.com/bintray/jcenter

Add the artifact dependency
---------------------------

- The group id is ``com.github.jcornaz.kwik``
- Pick an artifact in the `Available artifacts`_.
    - For a quick start on the JVM, chose ``kwik-core-jvm``
- Pick a version from: https://github.com/jcornaz/kwik/releases

Available artifacts
......................................

The best to start writing property tests in your project, the best module to use is certainly ``kwik-core-jvm`` (and ``kwik-core-common`` in Kotlin/Common modules).

But to write a module or project that extends Kwik (like a collection of generator),
you might prefer to pick more specific module(s) in this list:

.. list-table:: List of available modules
    :header-rows: 1

    * - Module
      - Kotlin/Common artifact
      - Kotlin/JVM artifact
      - Remarks
    * - Generator API
      - ``kwik-generator-api-common``
      - ``kwik-generator-api-jvm``
      - API to create new generators
    * - Standard generators
      - ``kwik-generator-stdlib-common``
      - ``kwik-generator-stdlib-jvm``
      - Generators for types available in the Kotlin standard library
    * - Property evaluator
      - ``kwik-property-common``
      - ``kwik-property-jvm``
      - Property evaluation
    * - Core
      - ``kwik-core-jvm-common``
      - ``kwik-core-jvm-jvm``
      - Contains nothing more than transitive dependencies on evaluator and standard generators

Example with gradle
...................

.. include:: ../README.rst
    :start-after: .. startGradleSetup
    :end-before: .. endGradleSetup

.. _configure-default-iterations:

Configure the default number of iterations
------------------------------------------

By default Kwik will evaluate each property 200 times. (each time with different random inputs)

This default can be configured by setting the system property ``kwik.iterations``.

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
on the CI server. (to make it work the server needs to have a ``CI`` environment variable)

And any developer may run ``./gradlew test -Dkwik.properties=10`` if he wants a fast feedback loop,
evaluating each property only 10 times.

.. note::
    The number of iteration defined when invoking ``forAll`` has precedence over the system property.

    See :ref:`how to choose number of iterations for specific property <choose-property-iterations>`
