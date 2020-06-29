Kwik
====

.. image:: https://img.shields.io/badge/License-Apache%202.0-blue.svg
    :target: https://raw.githubusercontent.com/jcornaz/kwik/main/LICENSE

.. image:: https://github.com/jcornaz/kwik/workflows/Build/badge.svg
    :target: https://github.com/jcornaz/kwik/actions

.. image:: https://codecov.io/gh/jcornaz/kwik/branch/main/graph/badge.svg
    :target: https://codecov.io/gh/jcornaz/kwik

.. image:: https://readthedocs.org/projects/kwik/badge/?version=latest
    :target: https://kwik.readthedocs.io/en/latest/?badge=latest

.. image:: https://badges.gitter.im/kwik-test/community.svg
    :target: https://gitter.im/kwik-test/community

.. image:: https://api.bintray.com/packages/kwik/stable/kwik/images/download.svg
    :target: https://bintray.com/kwik/stable/kwik

.. afterBadges

Property-based testing library for Kotlin.

**Main features:**

* Test-engine agnostic
* Multiplatform
* No reflection
* Configurable built-in generators
* Easy way to create and combine generators
* Seeded generation for reproducible results

**Planned features:**

* Shrinking

.. startReferenceToDoc

Find more information on https://kwik.readthedocs.io

.. endReferenceToDoc

Status
------

The project is incubating and its API is subject to changes.

Please give it a try and write a feedback in the issues_ or discuss on gitter_.

.. _issues: https://github.com/jcornaz/kwik/issues
.. _gitter: https://gitter.im/kwik-test/community

How it looks like
-----------------

.. code-block:: kotlin

    class PlusOperatorTest {

        @Test
        fun isCommutative() = forAll { x: Int, y: Int ->
            x + y == y + x
        }

        @Test
        fun isAssociative() = forAll(iterations = 1000) { x: Int, y: Int, z: Int ->
            (x + y) + z == x + (y + z)
        }

        @Test
        fun zeroIsNeutral() = forAll(seed = -4567) { x: Int ->
            x + 0 == x
        }
    }

.. startUsageReference

For more information read the usage_ and look at the available generators_

.. _generators: https://kwik.readthedocs.io/en/latest/generators.html
.. _usage: https://kwik.readthedocs.io/en/latest/write-tests.html

.. endUsageReference


Motivation
----------

Property based testing is great and very powerful. But despite the fact that many good libraries already exist,
none of them fully fit my needs.

The known alternatives either:

* Are bound to a specific test-engine
* Can only be used when compiling kotlin to Java (and cannot be used in multi-platform projects)
* Relies on reflection, making the tests slower and make some errors detectable only at runtime
* Do not allow enough freedom and safety to customize existing generators
* Force the user to add unwanted dependencies in the classpath

Setup
-----

Example of setup using gradle.

.. startGradleSetup
.. code-block:: kotlin

    repositories {
        jcenter()
    }

    dependencies {
        testCompile("com.github.jcornaz.kwik:kwik-core-jvm:$kwikVersion")
    }
.. endGradleSetup

.. startReferenceToSetup

Find more detailed information in the setup_ instructions.

.. _setup: https://kwik.readthedocs.io/en/latest/setup.html

.. endReferenceToSetup

Contribute
----------

See `how to contribute`_

.. _`how to contribute`: https://github.com/jcornaz/kwik/blob/main/CONTRIBUTING.md
