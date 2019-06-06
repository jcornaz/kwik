Welcome to Kwik's documentation!
================================

.. toctree::
   :maxdepth: 2

   setup

Kwik is a property-based testing library for Kotlin.

Main features
-------------

- Test-engine agnostic (Mix properties with examples in the engine of your choice)
- Multiplatform
- No reflection (better compile-time feedback and faster to execute)
- Configurable built-in generators
- Easy way to create and combine generators

Status
------

The project is incubating and its API may change in the future.

Please give it a try and write your feedback in the issues_.

You may also ask questions and discuss about this project on gitter_

.. _issues: https://github.com/jcornaz/kwik/issues
.. _gitter: https://gitter.im/kwik-test/community

Usage
-----

.. code-block:: kotlin
    :caption: Example with Junit or equivalent

    class PlusOperatorTest {

        // Default config and generators
        @Test
        fun isCommutative() = forAll { x: Int, y: Int ->
            x + y == y + x
        }

        // With a given number of iterations
        @Test
        fun isAssociative() = forAll(iterations = 1000) { x: Int, y: Int, z: Int ->
            (x + y) + z == x + (y + z)
        }

        // Use a seed to get reproducible results (useful when investigating a failure in the CI for instance)
        @Test
        fun zeroIsNeutral() = forAll(seed = -4567) { x: Int ->
            x + 0 == x
        }

        // Configure or use a custom generator
        @Test
        fun addNegativeSubtracts() = forAll(Generator.ints(min = 0), Generator.ints(max = 0)) { x, y ->
            x + y <= x
        }

        // Create a custom generator
        val customGenerator1 = Generator.create { rng -> CustomClass(rng.nextInt(), rng.nextInt()) }

        // Combine generators
        val customGenerator2 = Generator.ints().zip(Generator.ints()) { x, y -> CustomClass(x, y) }
    }

Motivation
----------

Property based testing is great and very powerful. But despite the fact that many good libraries already exist,
none of them fully fit my needs.

**The existing alternatives often:**

- Are bound to a specific test-engine
- Can only be used in Java module (not in Koltin multiplatform modules)
- Relies on reflection, making them slower then it could be and make some error detectable only at runtime
- Some of them also force the user to add unwanted dependencies in his/her classpath
