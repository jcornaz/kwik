Kwik
====

.. image:: https://img.shields.io/badge/License-Apache%202.0-blue.svg
    :target: https://github.com/jcornaz/kwik/blob/main/LICENSE

.. image:: https://img.shields.io/bintray/v/kwik/stable/kwik?color=blue&label=version
    :target: https://bintray.com/kwik/stable/kwik

.. image:: https://img.shields.io/badge/kotlin-1.4-informational
    :target: https://kotl.in

.. image:: https://readthedocs.org/projects/kwik/badge/?version=latest
    :target: https://kwik.readthedocs.io/en/latest/?badge=latest

.. image:: https://img.shields.io/github/workflow/status/jcornaz/kwik/Build
    :target: https://github.com/jcornaz/kwik/actions?query=branch%3Amain+workflow%3ABuild

.. image:: https://img.shields.io/badge/workspace-zenhub-%236061be
    :target: https://app.zenhub.com/workspaces/kwik-5e3aa5f35d8a250b41d730e1/board

.. afterBadge

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

Please give it a try and write a feedback in the issues_.

For mode details about the future of kwik, here's a roadmap_.

.. _issues: https://github.com/jcornaz/kwik/issues
.. _roadmap: https://github.com/jcornaz/kwik/blob/main/ROADMAP.md

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

Requirements
------------

* Kotlin version: 1.4.0 or newer
* JDK version: 8, 11 or 15

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
