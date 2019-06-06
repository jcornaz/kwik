Write property tests
====================

Basic usage
-----------

To evaluate a property one must invoke the function ``forAll`` like this:

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Default config and generators
    :end-before: //endregion

``forAll`` Will generate random inputs and evaluate the content of the lambda 200 times.
If the lambda return false, it will immediately throws an ``AssertionError`` making the test fail.

.. note::

    Kwik can automatically generate values for ``Int``, ``Double``, ``Boolean`` and ``String``.
    For other type we have to `Create a custom generator`_

Choose the number of iterations
-------------------------------

You may customize the number of iteration by setting the argument ``iteration``.

The following property will be evaluated 1000 times:

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region With a given number of iterations
    :end-before: //endregion

Use a seed to get reproducible results
--------------------------------------

Because Kwik use random values, it is by definition non-deterministic. But sometimes we do want some determinism.
Let's say, for instance we observed a failure on the CI server once, how can be sure to reproduce it locally?

To solve this problem, Kwik use seeds. By default a random seed is used, and printed in the console.
So if you observe a failure in the CI, we simply look at the build-log to see what seed has been used.

Then we can pass the seed to ``forAll`` so that it always pass the same sequence of inputs.

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region Use a seed
    :end-before: //endregion

Customize generated values
--------------------------

To have good property tests we have to pass good inputs.

That's why the function ``forAll`` accepts *generators*, and all built-in *generators* can be configured.

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region Configure or use a custom generator
    :end-before: //endregion

Create a custom generator
-------------------------

But what if we want to use types which is not supported by Kwik, like a domain-specific one?

For this we can create a generators by implementing the interface ``Generator``.

But most of the time it may be simpler to call ``Generator.create``:

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Create a custom generator
    :end-before: //endregion

For enums or finite set of values we can use ``Generator.enum()`` and ``Generator.of()``:

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Create a generator for an enum
    :end-before: //endregion

Add samples
-----------

Testing against random value is great. But often some value have more interest to be tested than others.

These edge-cases can be added to a generator with the function ``withSamples``.

And since ``null`` is a quite common edge-case, there is a dedicated ``withNull`` function.

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Add samples
    :end-before: //endregion

The generation will always start by emitting the given samples.
They will also be emitted few times later so that they are tested more often than other arbitrary values.

We can configure how often they have to be emitted with argument ``ratio``,
corresponding of the requested ratio of sample vs random values.

.. note::
    All built-in generators already have some samples included.

    For instance ``Generator.ints()`` will make sure to test ``0``, ``1``, ``-1``, ``Int.MAX_VALUE`` and ``Int.MIN_VALUE``