Generator operators
===================

Few operators are available as extension function on ``Generator`` to easily derive existing generators.


``Generator<T>.withSamples(vararg samples: T, ratio: Int = 0.2): Generator<T>``
    add the given samples into the generated values

    the generated values will start by the samples, and then it will make sure that the ratio of samples/random values
    stay >= to the given ratio

``Generator<T>.withNull(ratio: Int = 0.2): Generator<T?>``
    add ``null`` into the generated values

    the generated values will start by ``null``, and then it will make sure that the ratio of ``null``/random values
    stay >= to the given ratio

``Generator<T>.map(transform: (T) -> R): Generator<R>``
    apply a transformation to all elements emitted by the source generator

``Generator<T>.filter(predicate: (T) -> Boolean): Generator<T>``
    filter elements emitted by the source generator, so that only elements matching the predicate are emitted.

    Be aware that the property evaluation will then have to generate more values.

    Always favor other method of creating a generators or at least make sure that most of values will pass the predicate.

``Generator<T>.filterNot(predicate: (T) -> Boolean): Generator<T>``
    filter elements emitted by the source generator, so that only elements not matching the predicate are emitted.

    Be aware that the property evaluation will then have to generate more values.

    Always favor other method of creating a generators or at least make sure that most of values will pass the predicate.

``Generator<A>.combineWith(other: Generator<B>, transform: (A, B) -> R): Generator<Pair<T1, T2>>``
    Combine the generated values of both generators.

    Not specifying the transform, will combine the value in pairs.
