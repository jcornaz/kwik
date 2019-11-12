.. _operators:

Generator operators
===================

Few operators are available as extension function on ``Generator`` to easily derive existing generators.


``withSamples(vararg samples: T)``
    add the given samples into the generated values, making sure the samples are always tested

``withNull()``
    add ``null`` into the generated values, making sure is is always tested

``withNaN()``
    add ``NaN`` into the generated values, making sure is is always tested

    (for double generators only)

``map(transform: (T) -> R)``
    apply a transformation to all elements emitted by the source generator

``andThen(transform: (T) -> Generator<R>)``
    like map, it applies a transformation to all elements emitted by the source generator. The only difference
    is that ``transform`` returns a ``generator`` instead of a value. You may see it like a ``flatMap``.

``filter(predicate: (T) -> Boolean)``
    filter elements emitted by the source generator, so that only elements matching the predicate are emitted.

    Be aware that the property evaluation will then have to generate more values.

    Always favor other method of creating a generators or at least make sure that most of values will pass the predicate.

``filterNot(predicate: (T) -> Boolean)``
    filter elements emitted by the source generator, so that only elements not matching the predicate are emitted.

    Be aware that the property evaluation will then have to generate more values.

    Always favor other method of creating a generators or at least make sure that most of values won't pass the predicate.

Combining exiting operators
---------------------------

``combineWith(other: Generator<B>, transform: (A, B) -> R)``
    Combine the generated values of both generators.

    Generated values will start by a combination of the 5 first samples of both generators.
    Then samples of each generator have a higher probability to appear than other random values.

    Not specifying the transform, will combine the value in pairs.

``plus(other: Generator<T>)`` (can be used as ``+``)
    Merge the generated values of both operators. (each generator having the same probability to used)

``frequency(vararg weightedGenerators: Pair<Double, Generator<T>>)``
    Returns a generator that randomly pick a value from the given list of the generator according to their respective weights.
