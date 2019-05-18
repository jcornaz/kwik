package com.github.jcornaz.kwik

import kotlin.random.Random

private const val DEFAULT_SAMPLE_RATIO = 0.2

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 * using the provided [transform] function applied to each pair of elements.
 */
fun <A, B, R> Generator<A>.zip(
    other: Generator<B>,
    transform: (A, B) -> R
): Generator<R> = MergingGenerator(this, other, transform)

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 */
infix fun <A, B> Generator<A>.zip(
    other: Generator<B>
): Generator<Pair<A, B>> = MergingGenerator(this, other) { a, b -> a to b }

private class MergingGenerator<A, B, R>(
    private val generator1: Generator<A>,
    private val generator2: Generator<B>,
    private val transform: (A, B) -> R
) : Generator<R> {
    override fun randoms(seed: Long): Sequence<R> =
        generator1.randoms(seed).zip(generator2.randoms(seed), transform)
}

/**
 * Returns a generator containing the results of applying the given transform function to each element emitted by
 * the original generator.
 */
fun <T, R> Generator<T>.map(transform: (T) -> R): Generator<R> = MapGenerator(this, transform)

private class MapGenerator<T, R>(private val source: Generator<T>, private val transform: (T) -> R) : Generator<R> {
    override fun randoms(seed: Long): Sequence<R> = source.randoms(seed).map(transform)
}

/**
 * Returns a new generator adding the given [samples] into generated random values.
 *
 * The "random" values always start by the given [samples] so that they always appear at least once.
 *
 * @param ratio Ratio of random values which should be picked from the [samples].
 */
fun <T> Generator<T>.withSamples(vararg samples: T, ratio: Double = DEFAULT_SAMPLE_RATIO): Generator<T> =
    withSamples(samples.toList(), ratio)

/**
 * Returns a new generator adding the given [samples] into generated random values.
 *
 * The "random" values always start by the given [samples] so that they always appear at least once.
 *
 * @param ratio Ratio of random values which should be picked from the [samples].
 */
fun <T> Generator<T>.withSamples(samples: Collection<T>, ratio: Double = DEFAULT_SAMPLE_RATIO): Generator<T> =
    SampleGenerator(this, samples, ratio)

/**
 * Returns a new generator adding `null` into generated random values.
 *
 * The "random" values always start by `null` so that it always appear at least once.
 *
 * @param ratio Ratio of random values which should be `null`.
 */
fun <T> Generator<T>.withNull(ratio: Double = DEFAULT_SAMPLE_RATIO): Generator<T?> =
    NullGenerator(this, ratio)



private class SampleGenerator<T>(
    private val source: Generator<T>,
    private val samples: Collection<T>,
    private val ratio: Double
) : Generator<T> {

    init {
        require(samples.isNotEmpty()) { "No sample provided" }
        require(ratio in 0.0..1.0) { "Invalid ratio: $ratio" }
    }

    override fun randoms(seed: Long): Sequence<T> = source.randoms(seed).withSamples(samples, ratio, seed)
}

private class NullGenerator<T>(private val source: Generator<T>, private val ratio: Double) : Generator<T?> {
    init {
        require(ratio in 0.0..1.0) { "Invalid ratio: $ratio" }
    }

    override fun randoms(seed: Long): Sequence<T?> = source.randoms(seed).withSamples(listOf(null), ratio, seed)
}

private fun <T> Sequence<T>.withSamples(samples: Collection<T>, ratio: Double, seed: Long): Sequence<T> {
    return samples.asSequence() + sequence {
        val sourceValues = iterator()
        val rng = Random(seed)

        var sampleEmitted = samples.size
        var valuesEmitted = sampleEmitted

        while (true) {
            if (sampleEmitted < valuesEmitted * ratio) {
                yield(samples.random(rng))
                sampleEmitted++
            } else {
                yield(sourceValues.next())
            }
            valuesEmitted++
        }
    }
}
