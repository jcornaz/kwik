Generator operators
===================

Few operators are available as extension function on ``Generator`` to easily derive existing generators.


``withSamples(vararg samples: T, ratio: Int = 0.2)``
    add the given samples into the generated values

    the generated values will start by the samples, and then it will make sure that the ratio of samples/random values
    stay >= to the given ratio

``withNull(ratio: Int = 0.2)``
    add ``null`` into the generated values

    the generated values will start by ``null``, and then it will make sure that the ratio of ``null``/random values
    stay >= to the given ratio

``map(transform: (T) -> R)``
    apply a transformation to all elements emitted by the source generator

``filter(predicate: (T) -> Boolean)``
    filter elements emitted by the source generator, so that only elements matching the predicate are emitted.

    Be aware that the property evaluation will then have to generate more values.

    Always favor other method of creating a generators or at least make sure that most of values will pass the predicate.

``filterNot(predicate: (T) -> Boolean)``
    filter elements emitted by the source generator, so that only elements not matching the predicate are emitted.

    Be aware that the property evaluation will then have to generate more values.

    Always favor other method of creating a generators or at least make sure that most of values won't pass the predicate.

``combineWith(other: Generator<B>, transform: (A, B) -> R)``
    Combine the generated values of both generators.

    Not specifying the transform, will combine the value in pairs.
