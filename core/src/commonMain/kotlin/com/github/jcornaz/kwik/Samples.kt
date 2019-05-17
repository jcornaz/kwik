package com.github.jcornaz.kwik

import kotlin.random.Random

private const val DEFAULT_SAMPLE_RATIO = 0.2

/**
 * Returns a new generator adding the given [samples] into generated random values.
 *
 * The "random" values always start by the given [samples] so that they always appear at least once.
 *
 * @param ratio Ratio of random values which should be picked from the [samples].
 */
fun <T> Generator<T>.withSamples(vararg samples: T, ratio: Double = DEFAULT_SAMPLE_RATIO): Generator<T> =
    SampleGenerator(this, samples.toList(), ratio)

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
    private val samples: List<T>,
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

private fun <T> Sequence<T>.withSamples(samples: List<T>, ratio: Double, seed: Long): Sequence<T> {
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
