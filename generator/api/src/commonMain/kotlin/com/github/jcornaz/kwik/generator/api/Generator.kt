package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.api.simplification.SimplificationTree
import com.github.jcornaz.kwik.generator.api.simplification.simplestValue
import kotlin.random.Random

/**
 * Random data generator capable of producing test data for property testing.
 */
interface Generator<out T> {

    /**
     * Returns a random value using the given [random].
     *
     * This function can mutate [random]. But it should always return the same value for a given [Random] state.
     * Ex. `generate(Random(0L))` should always return the same value.
     */
    @Deprecated("Use generateSampleTree instead", ReplaceWith("generateSampleTree(random).root"))
    @UseExperimental(ExperimentalKwikGeneratorApi::class)
    fun generate(random: Random): T =
        generateSampleTree(random).root

    /**
     * Returns a random value using the given [random].
     *
     * The random value is wrapped in a [SimplificationTree] that can be used during input simplification process.
     *
     * This function can mutate [random]. But it should always return the same value for a given [Random] state.
     * Ex. `generate(Random(0L))` should always return the same value.
     */
    @ExperimentalKwikGeneratorApi
    fun generateSampleTree(random: Random): SimplificationTree<T>

    companion object {

        /**
         * Create a simple random [Generator].
         *
         * @param next Function that will be invoked to get a new random parameter.
         *             The function should use the given [Random] generator to ensure predictability of the values
         */
        @UseExperimental(ExperimentalKwikGeneratorApi::class)
        fun <T> create(next: (Random) -> T): Generator<T> = object : Generator<T> {
            override fun generateSampleTree(random: Random): SimplificationTree<T> =
                simplestValue(next(random))
        }

        /**
         * Create a random [Generator] generating values out of the given [samples]
         */
        fun <T> of(samples: Iterable<T>): Generator<T> {
            val list = if (samples is List) samples else samples.toList()

            require(list.isNotEmpty()) { "No given sample" }

            return create { list.random(it) }
        }

        /**
         * Create a random [Generator] generating values out of the given [samples]
         */
        fun <T> of(vararg samples: T): Generator<T> = of(samples.asList())
    }
}

/**
 * Returns a sequence of random values.
 *
 * @param seed Seed of the random generation.
 *             Two invocations of the function with the same seed must return the same value sequence
 */
@ExperimentalKwikGeneratorApi
fun <T> Generator<T>.randomSequence(seed: Long): Sequence<T> =
    sampleTreeSequence(seed).map { it.root }


/**
 * Returns a sequence of random values.
 *
 * @param seed Seed of the random generation.
 *             Two invocations of the function with the same seed must return the same value sequence
 */
@ExperimentalKwikGeneratorApi
fun <T> Generator<T>.sampleTreeSequence(seed: Long): Sequence<SimplificationTree<T>> =
    randomSequence(seed) { generateSampleTree(it) }

/**
 * Returns a sequence of random values.
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
