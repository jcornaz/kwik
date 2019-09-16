package com.github.jcornaz.kwik.generator.api

import kotlin.random.Random

/**
 * Random data generator capable of producing test data for property testing.
 */
interface Generator<T> {

    /**
     * Samples of values that should always be tested
     */
    val samples: Set<T>

    /**
     * Returns a sequence of random value.
     *
     * **Must be pure**: It must always return the same value sequence for the same given [seed]
     *
     * @param seed Seed of the random generation.
     *             Two invocations of the function with the same seed must return the same value sequence
     */
    @Deprecated("Use randomSequence extension function instead", ReplaceWith("randomSequence(seed)"))
    fun randoms(seed: Long): Sequence<T> = randomSequence(seed)

    fun generate(random: Random): T

    companion object {

        /**
         * Create a simple random [Generator].
         *
         * @param next Function that will be invoked to get a new random parameter.
         *             The function should use the given [Random] generator to ensure predictability of the values
         */
        fun <T> create(next: (Random) -> T): Generator<T> = object :
            Generator<T> {
            override val samples: Set<T> get() = emptySet()

            override fun generate(random: Random): T = next(random)
        }

        /**
         * Create a random [Generator] generating values out of the given [samples]
         */
        fun <T> of(vararg samples: T): Generator<T> {
            require(samples.isNotEmpty()) { "No given sample " }

            return create { samples.random(it) }
        }
    }
}

/**
 * Returns a sequence of random value.
 *
 * **Must be pure**: It must always return the same value sequence for the same given [seed]
 *
 * @param seed Seed of the random generation.
 *             Two invocations of the function with the same seed must return the same value sequence
 */
fun <T> Generator<T>.randomSequence(seed: Long): Sequence<T> =
    randomSequence(seed) { generate(it) }

/**
 * Returns a random sequence.
 *
 * @param seed Seed of the random generation.
 *             Two invocations of the function with the same seed will return the same value sequence
 * @param next Function that will be invoked to get a new random parameter.
 *             The function should use the given [Random] generator to ensure predictability of the values
 */
fun <T> randomSequence(seed: Long, next: (Random) -> T): Sequence<T> = sequence {
    val rng = Random(seed)

    while (true) yield(next(rng))
}
