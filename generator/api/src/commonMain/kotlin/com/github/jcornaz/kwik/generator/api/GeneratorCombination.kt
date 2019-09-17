package com.github.jcornaz.kwik.generator.api

import kotlin.random.Random

private const val NUMBER_OF_SAMPLES_FOR_COMBINATION = 5

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 * using the provided [transform] function applied to each pair of elements.
 */
fun <A, B, R> Generator<A>.combineWith(
    other: Generator<B>,
    transform: (A, B) -> R
): Generator<R> =
    CombinedGenerators(this, other, transform)

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 */
fun <A, B> Generator<A>.combineWith(
    other: Generator<B>
): Generator<Pair<A, B>> =
    CombinedGenerators(this, other, ::Pair)

/**
 * Returns a generator of combining the elements of [generator1] and [generator2]
 * using the provided [transform] function applied to each pair of elements.
 */
fun <A, B, R> Generator.Companion.combine(
    generator1: Generator<A>,
    generator2: Generator<B>,
    transform: (A, B) -> R
): Generator<R> =
    CombinedGenerators(generator1, generator2, transform)

/**
 * Returns a generator of combining the elements of [generator1] and [generator2]
 */
fun <A, B> Generator.Companion.combine(
    generator1: Generator<A>,
    generator2: Generator<B>
): Generator<Pair<A, B>> =
    CombinedGenerators(generator1, generator2, ::Pair)

private class CombinedGenerators<A, B, R>(
    private val generator1: Generator<A>,
    private val generator2: Generator<B>,
    private val transform: (A, B) -> R
) : Generator<R> {
    override val samples: Set<R> = generator1.samples.take(NUMBER_OF_SAMPLES_FOR_COMBINATION)
        .flatMapTo(mutableSetOf()) { a ->
            generator2.samples.take(NUMBER_OF_SAMPLES_FOR_COMBINATION).map { b -> transform(a, b) }
        }

    override fun generate(random: Random): R =
        transform(generator1.generate(random), generator2.generate(random))
}

/**
 * Returns a generator merging values of with the [other] generator
 */
operator fun <T> Generator<T>.plus(other: Generator<T>): Generator<T> =
    MergedGenerators(this, other)

private class MergedGenerators<T>(
    private val generator1: Generator<T>,
    private val generator2: Generator<T>
) : Generator<T> {
    override val samples: Set<T> = generator1.samples + generator2.samples

    override fun generate(random: Random): T =
        if (random.nextBoolean()) generator1.generate(random) else generator2.generate(random)
}
