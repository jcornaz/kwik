Kwik
====

.. image:: https://img.shields.io/badge/License-Apache%202.0-blue.svg
    :target: https://raw.githubusercontent.com/jcornaz/kwik/develop/LICENSE

.. image:: https://img.shields.io/badge/status-incubating-orange.svg
    :target: https://gist.githubusercontent.com/jcornaz/46736c3d1f21b4c929bd97549b7406b2/raw/ProjectStatusFlow

.. image:: https://github.com/jcornaz/kwik/workflows/Build/badge.svg
    :target: https://github.com/jcornaz/kwik/actions

.. image:: https://readthedocs.org/projects/kwik/badge/?version=latest
    :target: https://kwik.readthedocs.io/en/latest/?badge=latest

.. image:: https://badges.gitter.im/kwik-test/community.svg
    :target: https://gitter.im/kwik-test/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badg

.. image:: https://img.shields.io/badge/dynamic/json.svg?color=blue&label=version&query=name&url=https%3A%2F%2Fapi.bintray.com%2Fpackages%2Fkwik%2Fstable%2Fkwik%2Fversions%2F_latest
    :target: https://bintray.com/kwik/stable/kwik/_latestVersion

.. image:: https://img.shields.io/badge/dynamic/json.svg?color=blue&label=preview&query=name&url=https%3A%2F%2Fapi.bintray.com%2Fpackages%2Fkwik%2Fpreview%2Fkwik%2Fversions%2F_latest
    :target: https://bintray.com/kwik/preview/kwik/_latestVersion

.. afterBadges

Property-based testing library for Kotlin.

**Main features:**

* Test-engine agnostic
* Multiplatform
* No reflection
* Configurable built-in generators
* Easy way to create and combine generators

**Planned features:**

* Shrinking

.. startReferenceToDoc

Find more information on https://kwik.readthedocs.io

.. endReferenceToDoc

Status
------

The project is incubating and its API is subject to changes.

Please give it a try and write a feed back in the issues_ or discuss on gitter_.

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

The existing alternatives often:

* Are bound to a specific test-engine
* Can only be used in Java module (not in Kotlin multi-platform modules)
* Relies on reflection, making them slower than it could be and make some errors detectable only at runtime
* Some of them also force the user to add unwanted dependencies in the classpath

Setup
-----

Example of setup using gradle.

.. startGradleSetup
.. code-block:: kotlin

    repositories {
        jcenter()
        maven { url = uri("https://dl.bintray.com/kwik/preview") }
    }

    dependencies {
        testCompile("com.github.jcornaz.kwik:kwik-core-jvm:0.2.0-beta.2")
    }
.. endGradleSetup

.. startReferenceToSetup

Find more detailed information in the setup_ instructions.

.. _setup: https://kwik.readthedocs.io/en/latest/setup.html

.. endReferenceToSetup

Contribute
----------

See `how to contribute`_

.. _`how to contribute`: https://github.com/jcornaz/kwik/blob/master/CONTRIBUTING.md
