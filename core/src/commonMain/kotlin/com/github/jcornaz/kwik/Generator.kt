package com.github.jcornaz.kwik

import kotlin.random.Random

/**
 * Random data generator capable of producing test data for property testing.
 */
interface Generator<T> {

    /**
     * Returns a sequence of random value.
     *
     * **Must be pure**: It must always return the same value sequence for the same given [seed]
     *
     * @param seed Seed of the random generation.
     *             Two invocations of the function with the same seed must return the same value sequence
     */
    fun randoms(seed: Long): Sequence<T>

    companion object {

        /**
         * Create a simple random [Generator].
         *
         * @param next Function that will be invoked to get a new random parameter.
         *             The function should use the given [Random] generator to ensure predictability of the values
         */
        fun <T> create(next: (Random) -> T): Generator<T> = object : Generator<T> {
            override fun randoms(seed: Long): Sequence<T> = randomSequence(seed, next)
        }
    }
}

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
