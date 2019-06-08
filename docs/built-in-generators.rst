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

``Generator.longs(min = Long.MIN_VALUE, max = Long.MAX_VALUE)``
    Generate longs. Includes the samples: ``0``, ``1``, ``-1``, ``min`` and ``max``.

``Generator.floats(min = -Float.MAX_VALUE, max = Float.MAX_VALUE)``
    Generate longs. Includes the samples: ``0.0``, ``1.0``, ``-1.0``, ``min`` and ``max``.

    Note that ``NaN``, ``POSITIVE_INFINITY`` and ``POSITIVE_INFINITY`` are not generated.
    To test theses, we can use ``withSamples()``

    Example: ``Generator.floats().withSamples(Double.NaN)``

``Generator.doubles(min = -Double.MAX_VALUE, max = Double.MAX_VALUE)``
    Generate doubles. Includes the samples: ``0.0``, ``1.0``, ``-1.0``, ``min`` and ``max``.

    Note that ``NaN``, ``POSITIVE_INFINITY`` and ``POSITIVE_INFINITY`` are not generated.
    To test theses, we can use ``withSamples()``

    Example: ``Generator.doubles().withSamples(Float.NaN)``

``Generator.booleans()``
    Generate booleans


Strings
-------

``Generator.strings(minLength = 0, maxLength = 200, charset = PRINTABLE_CHARACTERS, exclude = emptySet())``
    Generate strings. Use the parameter ``charset`` and ``exclude`` to customize the characters which can be used.

    Generation include empty ("") and blank (" ") strings.

    Strings will be generated of any size between ``minLength`` and ``maxLength``,
    but there is higher probability of generation for the smallest sizes.


Collections
-----------

``Generator.lists(elementGen = Generator.default(), minSize = 0, maxSize = 200)``
    Generate lists. ``elementGen`` can be used to define the generator of the elements.

    Generation include empty lists

    Lists will be generated of any size between ``minSize`` and ``maxSize``,
    but there is higher probability of generation for the smallest sizes.

``Generator.sets(elementGen = Generator.default(), minSize = 0, maxSize = 200)``
    Generate sets. ``elementGen`` can be used to define the generator of the elements.

    Generation include empty sets

    Sets will be generated of any size between ``minSize`` and ``maxSize``,
    but there is higher probability of generation for the smallest sizes.

    Will fail in it takes too much iteration to reach the ``minSize``
    (so make sure the element generator can generate enough different values)

``Generator.maps(keyGen = Generator.default(), valueGen = Generator.default(), minSize = 0, maxSize = 200)``
    Generate sets. ``keyGen`` can be used to define the generator of the elements.

    Generation include empty maps

    Maps will be generated of any size between ``minSize`` and ``maxSize``,
    but there is higher probability of generation for the smallest sizes.

    Will fail in it takes too much iteration to reach the ``minSize``
    (so make sure the element generator can generate enough different values)


Enums
-----

``Generator.enum<T>()``
    Create a generator for the given enum type ``T``.

    The enum must contains at least one enumeration.
