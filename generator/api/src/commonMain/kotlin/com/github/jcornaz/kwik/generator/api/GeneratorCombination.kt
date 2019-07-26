package com.github.jcornaz.kwik.generator.api

import kotlin.random.Random

private const val SAMPLE_PROBABILITY = 0.15
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

    override fun randoms(seed: Long): Sequence<R> =
        generator1.randomWithSamples(seed)
            .zip(generator2.randomWithSamples(seed + 1), transform)

    private fun <T> Generator<T>.randomWithSamples(seed: Long): Sequence<T> {
        if (samples.isEmpty()) return randoms(seed)

        return sequence {
            val rng = Random(seed)
            val values = randoms(seed).iterator()

            while (true) {
                yield(if (rng.nextDouble() < SAMPLE_PROBABILITY) samples.random(rng) else values.next())
            }
        }
    }
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

    override fun randoms(seed: Long): Sequence<T> = sequence {
        val iterator1 = generator1.randoms(seed).iterator()
        val iterator2 = generator2.randoms(seed + 1).iterator()

        while (true) {
            yield(iterator1.next())
            yield(iterator2.next())
        }
    }
}
