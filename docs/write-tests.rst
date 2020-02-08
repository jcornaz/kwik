Write property tests
====================

Basic usage
-----------

To evaluate a property we must invoke the function ``forAll`` like this:

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Default config and generators
    :end-before: //endregion

``forAll`` Will generate random inputs and evaluate the content of the lambda 200 times.
If the lambda return false, it will immediately throws an ``AssertionError`` making the test fail.

So the test pass only if the lambda returns true for 200 random inputs.

.. note::
    Kwik can automatically generate values for ``Int``, ``Double``, ``Boolean`` and ``String``.

    For other types we have to `Create a custom generator`_

Use assertions
--------------

If writing a lambda that return a boolean is not of your taste, you may alternatively use `checkForAll`.
Instead of returning a boolean, we have to throw an exception in case of falsification.

Example:

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Using checkForAll
    :end-before: //endregion

This alternative can be especially useful to get more descriptive messages. In the example above, a falsification of the
property would display the expected and actual values. Theses kind of messages cannot be provided when using `forAll`.


.. _choose-property-iterations:

Choose the number of iterations
-------------------------------

By default the property is evaluated 200 times [1]_. But we can configure it by setting the argument ``iteration``.

For instance, the following property will be evaluated 1000 times:

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region With a given number of iterations
    :end-before: //endregion

.. [1] The default number of iterations can be :ref:`configured via system property <configure-default-iterations>`

Use a seed to get reproducible results
--------------------------------------

Because Kwik use random values, it is by definition non-deterministic. But sometimes we do want some determinism.
Let's say, for instance we observed a failure on the CI server once, how can be sure to reproduce it locally?

To solve this problem, Kwik use seeds. By default a random seed is used and printed in the console.
If we observe a failure in the CI, we simply look at the build-log to see what seed has been used,
then we can pass the seed to ``forAll`` so that it always test the same inputs.

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region Use a seed
    :end-before: //endregion

Customize generated values
--------------------------

Random input is good. But sometimes, we need to constraint the range of possible inputs.

That's why the function ``forAll`` accepts *generators*, and all built-in *generators* can be configured.

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region Configure or use a custom generator
    :end-before: //endregion

Create a custom generator
-------------------------

But what if we want to test with input types which are not supported by Kwik, like domain-specific ones?

For this we can create a generator by implementing the interface ``Generator``.

But most of the time it may be simpler to call ``Generator.create``:

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Create a custom generator
    :end-before: //endregion

For enums or finite set of values we can use ``Generator.enum()`` and ``Generator.of()``:

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Create a generator for an enum
    :end-before: //endregion

.. note::
    You may reuse existing operator to build new ones. This can be done by calling ``Genarator.genarate(Random)`` of other
    operators, or by using the available :ref:`operators <operators>`

Add samples
-----------

Testing against random value is great. But often some value have more interest to be tested than others.

These edge-cases can be added to a generator with the function ``withSamples``.

And since ``null`` and ``NaN`` are two quite common edge-case, there are dedicated ``withNull`` and ``withNaN`` operators.

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 4
    :start-after: //region Add samples
    :end-before: //endregion

The samples have higher chance to be generated and will be tested more often.

.. note::
    All built-in generators already have some samples included.

    For instance ``Generator.ints()`` will generate ``0``, ``1``, ``-1``, ``Int.MAX_VALUE`` and ``Int.MIN_VALUE`` often.

Skip an evaluation
------------------

Sometime we want to exclude some specific set of input. For that, we can call ``skipIf`` in the property evaluation block.

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/PlusOperatorExample.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region Skip evaluation
    :end-before: //endregion

Be careful to not overuse it though as it may slow down the tests.
Always prefer creating or configuring custom generators if you can.

Make sure that a condition is satisfied at least once
-----------------------------------------------------

All theses random inputs are nice, but we may want to be sure that some conditions are met all the time.

For that, we can call ``ensureAtLeastOne``. It will force the property evaluation run as many time as necessary, so that
the given predicate gets true.

.. literalinclude:: ../example/src/test/kotlin/com/github/jcornaz/kwik/example/EnsureAtLeastOneExample.kt
    :language: kotlin
    :dedent: 8
    :start-after: //region ensure at least one evaluation
    :end-before: //endregion


Be careful to not overuse it either as it may slow down the tests.
