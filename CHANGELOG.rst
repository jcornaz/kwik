Change log
==========

List of notable changes to this project.

The format is based on `Keep a Changelog`_,
and this project adheres to `Semantic Versioning`_.

.. _Keep a Changelog: https://keepachangelog.com/en/1.0.0
.. _Semantic Versioning: https://semver.org/spec/v2.0.0.html

**Breaking changes are written in bold**

Unreleased_
-----------

Added
.....
* `Generator.combine` as a style alternative to combine generators

Changed
.......

* **`zip` operator renamed to `combineWith` for better clarity and discoverability**

0.1.0-alpha.2_ - 2019-05-19
---------------------------

Added
.....

* `ints`, `longs`, `floats`, `doubles` and `boolean` generators
* `map` operator to transform an existing generator
* `Generator.of()` to create a generate from a finite set of samples
* `enum` to create a generator from an enum
* `strings` to create a String generator
* `default` Capable of inferring what generator to return for a given type.
* `lists`, `sets` and `maps` generators

Changed
.......

* **Default generator arguments added in `forAll` and `checkForAll`.**

Removed
.......

* **`checkForAll` functions as it was unsafe, allowing to forget assertions without compile-time error.**

0.1.0-alpha.1_ - 2019-05-18
---------------------------

Added
.....

* `Generator` interface for generating random values
* `randomSequence` helper to easily create a random (yet predictable) sequence of value
* `forAll` and `checkForAll` function to assess a property of the system under test.
* `withSample` and `withNull` to inject constants values to be always tested
* `zip` operator to combine two given generators

.. _Unreleased: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.2...HEAD
.. _0.1.0-alpha.2: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.1...0.1.0-alpha.2
.. _0.1.0-alpha.1: https://github.com/jcornaz/kwik/tree/0.1.0-alpha.1
