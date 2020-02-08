Configuration
=============

Kwik allow you to configure some defaults via system property (for Kotlin/JVM) or environment variable
(Kotlin/JVM, or Kotlin/Native on linux),

Note when running Kotlin/JVM the system properties have precedence over the environment variable
(in case they are both set)

.. _configure-default-iterations:

Default number of iterations
----------------------------

By default Kwik will evaluate each property 200 times. (each time with different random inputs)

This default can configured by defining the system property ``kwik.iterations``
or environment variable ``KWIK_ITERATIONS``.

That can be especially useful to define a different number of iteration on the CI server

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

    See :ref:`how to choose number of iterations for specific property <choose-iterations>`


.. _configure-default-seed:

Default seed
------------

By default Kwik will generate a different random seed for each property evaluation, leading to unpredicatable input.

That's generally desirable as over multiple build run, the test will cover more and more the domain of possible input.

But during debugging session, it is likely that one want perfectly reproducible builds.
That can be achieved by defining a seed either on the :ref:`property evaluation <choose-seed>`
, or globally via the system property ``kwik.seed`` or the environment variable ``KWIK_SEED``.

