Change log
==========

List of notable changes to this project.

The format is based on `Keep a Changelog`_,
and this project adheres to `Semantic Versioning`_.

.. _Keep a Changelog: https://keepachangelog.com/en/1.0.0
.. _Semantic Versioning: https://semver.org/spec/v2.0.0.html

Unreleased_
-----------

Added
.....

* **(Breaking)** ``PropertyEvaluationContext`` available as a type-receiver in property evaluation
* ``skipIf`` function on ``PropertyEvaluationContext`` allowing to skip a property evaluation for some specific set of input
* Few aliases to get common generators:
    * ``positiveInts``, ``naturalInts``, ``negativeInts`` and ``nonZeroInts``
    * ``positiveLongs``, ``naturalLongs``, ``negativeLongs`` and ``nonZeroLongs``
    * ``positiveFloats``, ``negativeFloats`` and ``nonZeroFloats``
    * ``positiveDoubles``, ``negativeDoubles`` and ``nonZeroDoubles``
    * ``nonEmptyStrings`` and ``nonBlankStrings``
    * ``nonEmptyLists``, ``nonEmptySets`` and ``nonEmptyMaps``

Changed
.......

* Show test details in console only in case of success
* Improved exception message in case of falsified property (and introduce typed exception ``FalsifiedPropertyError``

Dependencies Updated
....................

* Kotlin (from ``1.3.31`` to ``1.3.40``)

0.1.0-alpha.4_ - 2019-06-10
---------------------------

Added
.....

* ``filter`` operator for generators
* **(Breaking)** ``samples`` property in the ``Generator`` interface to improve management of the value samples (edge-cases)
* ``forAll`` non-inline function, allowing to hide implementation details.
* ``+`` operator for generator, allowing to merge 2 operators. Example: ``nonZeroInts() = ints(max = -1) + ints(min = 1)``

Changed
.......

* **(Breaking)** Renamed arguments ``from`` and ``until`` of ``floats`` and ``doubles`` generators
  for more consistency with the int and long generators
* **(Breaking)** Make the lambda of ``forAll`` crossinline, to allow usage of a non-inline function and hide implementation details.
* Now the size probability for strings a collections generators is similar for all sizes.
  Instead, they have empty and singletons instances as samples
* ``combine`` and ``combineWith`` now start by a combination of the source generators and will randomly add samples in the random generations.
* Prevent error when passing a big min size/length without a max size/length for collection and strings generators

Removed
.......

* **(Breaking)** ``ratio`` argument from the ``withSamples`` and ``withNull`` operators

0.1.0-alpha.3_ - 2019-06-08
---------------------------

Added
.....
* ``Generator.combine`` as a style alternative to combine generators

Changed
.......

* **(Breaking)** ``zip`` operator renamed to ``combineWith`` for better clarity and discoverability

0.1.0-alpha.2_ - 2019-05-19
---------------------------

Added
.....

* ``ints``, ``longs``, ``floats``, ``doubles`` and ``boolean`` generators
* ``map`` operator to transform an existing generator
* ``Generator.of()`` to create a generator from a finite set of samples
* ``enum`` to create a generator from an enum
* ``strings`` to create a String generator
* ``default`` Capable of inferring what generator to return for a given type
* ``lists``, ``sets`` and ``maps`` generators

Changed
.......

* **(Breaking)** Default generator arguments added in ``forAll`` and ``checkForAll``

Removed
.......

* **(Breaking)** ``checkForAll`` functions as it was unsafe, allowing to forget assertions without compile-time error

0.1.0-alpha.1_ - 2019-05-18
---------------------------

Added
.....

* ``Generator`` interface for generating random values
* ``randomSequence`` helper to easily create a random (yet predictable) sequence of value
* ``forAll`` and ``checkForAll`` function to assess a property of the system under test.
* ``withSample`` and ``withNull`` to inject constants values to be always tested
* ``zip`` operator to combine two given generators

.. _Unreleased: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.4...develop
.. _0.1.0-alpha.4: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.3...0.1.0-alpha.4
.. _0.1.0-alpha.3: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.2...0.1.0-alpha.3
.. _0.1.0-alpha.2: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.1...0.1.0-alpha.2
.. _0.1.0-alpha.1: https://github.com/jcornaz/kwik/tree/0.1.0-alpha.1
