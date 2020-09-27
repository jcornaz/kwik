Built-in generators
===================

Kwik provide a collection of generators to satisfy a wide variety of uses-cases.

They are all available as extension functions on ``Generator.Companion`` so that we can find them easily and invoke them like this:

.. code-block:: kotlin

    val generator = Generator.ints()

Primitives
----------

``Generator.ints(min = Int.MIN_VALUE, max = Int.MAX_VALUE)``
    Generate integers. Includes the samples: ``0``, ``1``, ``-1``, ``min`` and ``max``.

    Note that there are also ``positiveInts``, ``naturalInts``, ``negativeInts`` and ``nonZeroInts`` alternatives

``Generator.longs(min = Long.MIN_VALUE, max = Long.MAX_VALUE)``
    Generate longs. Includes the samples: ``0``, ``1``, ``-1``, ``min`` and ``max``.

    Note that there are also ``positiveLongs``, ``naturalLongs``, ``negativeLongs`` and ``nonZeroLongs`` alternatives

``Generator.floats(min = -Float.MAX_VALUE, max = Float.MAX_VALUE)``
    Generate longs. Includes the samples: ``0.0``, ``1.0``, ``-1.0``, ``min`` and ``max``.

    Note that ``NaN``, ``POSITIVE_INFINITY`` and ``POSITIVE_INFINITY`` are not generated.
    To test theses, we can use ``withSamples()`` or ``withNaN()``

    Example: ``Generator.floats().withNaN().withSamples(Float.POSITIVE_INFINITY)``

    Note that there are also ``positiveFloats``, ``negativeFloats`` and ``nonZeroFloats`` alternatives

``Generator.doubles(min = -Double.MAX_VALUE, max = Double.MAX_VALUE)``
    Generate doubles. Includes the samples: ``0.0``, ``1.0``, ``-1.0``, ``min`` and ``max``.

    Note that ``NaN``, ``POSITIVE_INFINITY`` and ``POSITIVE_INFINITY`` are not generated.
    To test theses, we can use ``withSamples()`` or ``withNaN()``

    Example: ``Generator.doubles().withNaN().withSamples(Double.POSITIVE_INFINITY)``

    Note that there are also ``positiveDoubles``, ``negativeDoubles`` and ``nonZeroDoubles`` alternatives

``Generator.booleans()``
    Generate booleans


Text
-------

``Generator.characters(charset = CharSets.printable, exclude = emptySet())``
    Generate strings. Use the parameter ``charset`` and ``exclude`` to customize the characters which can be used.

    Generation includes space (' ') as a sample.

    .. note:: ``CharSets`` provide few common set of characters such as ``alpha``, ``alphaNumeric`` and others

        It is there to help quickly configure the Character generator.

        By default, it will generate any printable characters.

``Generator.strings(minLength = 0, maxLength = 50, charGenerator = Generator.characters())``
    Generate strings. Use the parameter ``charGenerator`` to provide a character generator which is used to make the
    string.

    Generation includes empty ("") string as a sample.

Collections
-----------

``Generator.lists(elementGen = Generator.default(), minSize = 0, maxSize = 50)``
    Generate lists. ``elementGen`` can be used to define the generator of the elements.

    Generation include empty and singleton lists as samples

    Note that there is also a ``nonEmptyLists`` alternative

``Generator.sets(elementGen = Generator.default(), minSize = 0, maxSize = 50)``
    Generate sets. ``elementGen`` can be used to define the generator of the elements.

    Generation include empty and singleton sets as samples

    Will fail in it takes too much iteration to reach the ``minSize``
    (so make sure the element generator can generate enough different values)

    Note that there is also a ``nonEmptySets`` alternative

``Generator.maps(keyGen = Generator.default(), valueGen = Generator.default(), minSize = 0, maxSize = 50)``
    Generate sets. ``keyGen`` can be used to define the generator of the elements.

    Generation include empty and singleton maps as samples

    Will fail in it takes too much iteration to reach the ``minSize``
    (so make sure the element generator can generate enough different values)

    Note that there is also a ``nonEmptyMaps`` alternative

Sequences
---------

``Generator.sequences(elementGen = Generator.default(), minSize = 0, maxSize = 50)``
    Generate sequences. ``elementGen`` can be used to define the generator of the elements.

    Generation include empty and singleton sequences as samples

    Note that there is also a ``nonEmptySequences`` alternative

Enums
-----

``Generator.enum<T>()``
    Create a generator for the given enum type ``T``.

    The enum must contains at least one enumeration.

Java
----

``Generator.uuids()``
    Create a generator for UUID
