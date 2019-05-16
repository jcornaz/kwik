package com.github.jcornaz.kwik

import kotlin.random.Random

/**
 * Random data generator capable of producing test data for property testing.
 *
 * Despite its "random" property, it is expected to be immutable and pure functions.
 */
interface Generator<T> {

    /**
     * List of edge case that should always be tested
     *
     * Must be immutable.
     */
    val edgeCases: List<T>

    /**
     * Returns a sequence of random value.
     *
     * Must be pure: and It must always return the same value sequence one given [seed]
     *  * It should provide different sequence for different given [seed]
     *
     * @param seed Seed of the random generation.
     *             Two invocations of the function with the same seed must return the same value sequence
     */
    fun randoms(seed: Long): Sequence<T>

    companion object {

        /**
         * Create a random [Generator] without edge case.
         *
         * @param next Function that will be invoked to get a new random parameter.
         *             The function should use the given [Random] generator to ensure predictability of the values
         */
        fun <T> create(next: (Random) -> T): Generator<T> = object : Generator<T> {
            override val edgeCases: List<T>
                get() = emptyList()

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
