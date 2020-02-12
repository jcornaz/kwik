Change log
==========

List of notable changes to this project.

The format is based on `Keep a Changelog`_,
and this project adheres to `Semantic Versioning`_.

.. _Keep a Changelog: https://keepachangelog.com/en/1.0.0
.. _Semantic Versioning: https://semver.org/spec/v2.0.0.html

.. startUnreleasedBlock

Unreleased_
-----------

Added
.....

Fuzzer
,,,,,,

A new module ``fuzzer`` has been added. It contain an experimental abstraction layer which is meant to be above the
``Generator``.

The (existing)  Generator is responsible only for generating random value
(without even knowing if it is for testing purpose or not).

A ``Fuzzer`` is specialized for property based testing.
It contains a ``Generator``, a ``Simplifier`` and a list of guarantees
that must be satisfied at least once during evaluation.

A new function ``forAny`` is provided to run test using ``Fuzzer`` instead of ``Generator`` in module ``evaluator``.

In case of property falsification,
that ``forAny`` function simplify the input to the simplest input that still falsify the property.

.. endUnreleasedBlock

0.4.0_ - 2020-02-08
-------------------

Added
.....

* Allow to configure default number of iteration via environment variable ``KWIK_ITERATIONS`` on JVM and Linux
  (on JVM the system property has precedence over the environment variable)
* Allow to configure default seed via system property ``kwik.seed`` (on JVM) or via environment variable ``KWIK_SEED`` on JVM and Linux
  (on JVM the system property has precedence over the environment variable)


0.3.0_ - 2020-01-24
-------------------

Removed **(Breaking)**
......................

* Module ``core``. It was an empty module only to provide transitive dependencies to most common used modules.

Added
.....

* ``linux`` and ``windows`` targets (x64 arch)
    Thanks to drodel_ and michellesantos_

.. _drodel: https://github.com/drodel
.. _michellesantos: https://github.com/michellesantos

Migration from 0.2.0
....................

#. Before updating to 0.3.0 (so using version 0.2.0)

    #. Remove all usage of deprecated members

       * Use ``Generator.withSamples`` instead of ``Generator.samples``

       * Use ``Generator.randomSequence`` instead of ``Generator.randoms``

    #. Replace dependencies of module ``core`` by ``evaluator`` and ``generator-stdlib``
        Example with gradle for JVM project:

        .. code-block:: kotlin

            dependencies {
                testImplementation("com.github.jcornaz.kwik:kwik-evaluator-jvm:0.2.0")
                testImplementation("com.github.jcornaz.kwik:kwik-generator-stdlib-jvm:0.2.0")
            }


0.3.0-alpha.1_ - 2019-12-05
---------------------------

Removed **(Breaking)**
......................

* ``Generator.samples`` property has been removed.
    Now the generator have to have a probability to generate the edge cases in ``generate``.
    The recommended option when building a generator, is to use ``withSamples``

* ``Generator.randoms`` function has been removed.
    ``Generator.generate`` or ``Generator.randomSequence`` should be used instead.

Changed
.......

All generators no longer statically provide ``samples``. Instead they have a good probability to generate edge cases.

* **(Breaking binary)** ``withSample`` now takes a probability argument, and include the samples in the generation

Added
.....

* ``ensureAtLeastOne`` utility in evaluation context,
    that will force property evaluation until a given predicate gets satisfied
    Thanks to alessandroagnelli_

.. _alessandroagnelli: https://github.com/alessandroagnelli

Dependencies Updated
....................

* Kotlin (from ``1.3.60`` to ``1.3.61``)

0.2.0_ - 2019-11-19
-------------------

Deprecated
..........
* ``samples`` property in ``Generator`` interface. (Samples are about to be included in generation of ``generate``)
    One should use ``withSamples`` and ``frequency`` instead.

Added
.....
* ``andThen`` operator to make easier building complex generation pipeline
* ``frequency`` combinator, allowing to create a frequency-parametrized generator

Dependencies Updated
....................

* Kotlin (from ``1.3.50`` to ``1.3.60``)


0.2.0-rc.1_ - 2019-09-18
--------------------------

Changed
.......
* default max size/length for string,collection and sequence generators set to `50` (instead of `200`)

Added
.....
* ``sequences`` generator
* ``uuids`` generator

0.2.0-beta.2_ - 2019-09-16
--------------------------

Deprecated
..........

* Method ``Generator.randoms(Long)`` the generator should generate single values, not a sequence.

Added
.....

* **(Breaking)** method ``generate(Random): T`` in interface ``Generator`` to generate single value.
* ``checkForAll``. An alternative to ``forAll`` allowing to use assertion (throw in case of error) instead of returning a boolean.
  It can be especially helpful to more descriptive message about what is wrong.
* ``Generator.randomSequence`` extension function to replace the deprecated ``Generator.randoms(Long): Sequence<T>``

0.2.0-beta.1_ - 2019-09-15
--------------------------

Changed
.......

* Wrap exceptions thrown by the properties so that an helpful message can be displayed (number of attempts, arguments, etc.)

Dependencies Updated
....................

* Kotlin (from ``1.3.41`` to ``1.3.50``)

0.1.0_ - 2019-07-28
-------------------

Added
.....

* ``StringCharSets`` object with ``numeric``, ``alphaLowerCase``, ``alphaUpperCase``, ``alpha`` and ``alphaNumeric``.
    Sets of character to easily configure the string generator.
* ``kwik.iterations`` system property to globally define a default number of iteration.

Modules extracted from core
...........................

* Generator API moved to ``generator-api`` module
  (artifacts ``generator-api-common`` and ``generator-api-jvm``)
* Generators for types of the kotlin standard library is moved to `generator-stdlib` module
  (artifacts ``generator-stdlib-common`` and ``generator-stdlib-jvm``)
* The property evaluation is moved to `evaluator`
  (artifacts ``evaluator-common`` and ``evaluator-jvm``)

.. note:: The module ``core`` remains as an alias of all the modules above.
    So it is still easy to get started with Kwik by simply adding ``core`` as a dependency

Package names changed (Breaking)
................................

The packages have been renamed, and classes/files have been moved to reflect their new module (see `Modules extracted from core`_)

* **(Breaking)** The content ``com.github.jcornaz.kwik.generator`` as been moved to ``com.github.jcornaz.kwik.generator.stdlib``
* **(Breaking)** The content ``com.github.jcornaz.kwik`` as been splitted into ``com.github.jcornaz.kwik.generator.api`` and ``com.github.jcornaz.kwik.evaluator``

Dependencies Updated
....................

* Kotlin (from ``1.3.40`` to ``1.3.41``)

0.1.0-beta.1_ - 2019-07-01
--------------------------

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

.. _Unreleased: https://github.com/jcornaz/kwik/compare/0.4.0...master
.. _0.4.0: https://github.com/jcornaz/kwik/compare/0.3.0...0.4.0
.. _0.3.0: https://github.com/jcornaz/kwik/compare/0.3.0-alpha.1...0.3.0
.. _0.3.0-alpha.1: https://github.com/jcornaz/kwik/compare/0.2.0...0.3.0-alpha.1
.. _0.2.0: https://github.com/jcornaz/kwik/compare/0.2.0-rc.1...0.2.0
.. _0.2.0-rc.1: https://github.com/jcornaz/kwik/compare/0.2.0-beta.2...0.2.0-rc.1
.. _0.2.0-beta.2: https://github.com/jcornaz/kwik/compare/0.2.0-beta.1...0.2.0-beta.2
.. _0.2.0-beta.1: https://github.com/jcornaz/kwik/compare/0.1.0...0.2.0-beta.1
.. _0.1.0: https://github.com/jcornaz/kwik/compare/0.1.0-beta.1...0.1.0
.. _0.1.0-beta.1: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.4...0.1.0-beta.1
.. _0.1.0-alpha.4: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.3...0.1.0-alpha.4
.. _0.1.0-alpha.3: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.2...0.1.0-alpha.3
.. _0.1.0-alpha.2: https://github.com/jcornaz/kwik/compare/0.1.0-alpha.1...0.1.0-alpha.2
.. _0.1.0-alpha.1: https://github.com/jcornaz/kwik/tree/0.1.0-alpha.1
