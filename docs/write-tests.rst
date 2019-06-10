Write property tests
====================

Basic usage
-----------

To evaluate a property we must invoke the function ``forAll`` like this:

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Default config and generators
    :end-before: //endregion

``forAll`` Will generate random inputs and evaluate the content of the lambda 200 times.
If the lambda return false, it will immediately throws an ``AssertionError`` making the test fail.

So the test pass only if the lambda returns true for 200 random inputs.

.. note::
    Kwik can automatically generate values for ``Int``, ``Double``, ``Boolean`` and ``String``.
    For other type we have to `Create a custom generator`_

Choose the number of iterations
-------------------------------

By default the property is evaluated 200 times. But we can configure it by setting the argument ``iteration``.

For instance, the following property will be evaluated 1000 times:

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region With a given number of iterations
    :end-before: //endregion

Use a seed to get reproducible results
--------------------------------------

Because Kwik use random values, it is by definition non-deterministic. But sometimes we do want some determinism.
Let's say, for instance we observed a failure on the CI server once, how can be sure to reproduce it locally?

To solve this problem, Kwik use seeds. By default a random seed is used and printed in the console.
If we observe a failure in the CI, we simply look at the build-log to see what seed has been used,
then we can pass the seed to ``forAll`` so that it always test the same inputs.

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region Use a seed
    :end-before: //endregion

Customize generated values
--------------------------

Random input is good. But sometimes, we need to constraint the range of possible inputs.

That's why the function ``forAll`` accepts *generators*, and all built-in *generators* can be configured.

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region Configure or use a custom generator
    :end-before: //endregion

Create a custom generator
-------------------------

But what if we want to test with input types which are not supported by Kwik, like domain-specific ones?

For this we can create a generator by implementing the interface ``Generator``.

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

And since ``null`` and ``NaN`` are two quite common edge-case, there are dedicated ``withNull`` and ``withNaN`` operators.

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Add samples
    :end-before: //endregion

The generation will always start by emitting the given samples.

.. note::
    All built-in generators already have some samples included.

    For instance ``Generator.ints()`` will make sure to test ``0``, ``1``, ``-1``, ``Int.MAX_VALUE`` and ``Int.MIN_VALUE``

Skip an evaluation
------------------

Sometime we want to exclude some specific set of input. For that, we can call ``skipIf`` in the property evaluation block.

.. literalinclude:: ../core/src/commonTest/kotlin/com/github/jcornaz/kwik/example/PlusOperatorTest.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Skip evaluation
    :end-before: //endregion

Be careful to not overuse it though as it may slow down the tests.
Always prefer creating or configuring custom generators if you can.
