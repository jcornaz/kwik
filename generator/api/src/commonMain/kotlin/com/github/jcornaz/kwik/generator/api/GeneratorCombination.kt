package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.api.simplification.SampleTree
import com.github.jcornaz.kwik.generator.api.simplification.sampleLeaf
import kotlin.random.Random

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 * using the provided [transform] function applied to each pair of elements.
 */
fun <A, B, R> Generator<A>.combineWith(
    other: Generator<B>,
    transform: (A, B) -> R
): Generator<R> =
    Generator.combine(this, other, transform)

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 */
fun <A, B> Generator<A>.combineWith(
    other: Generator<B>
): Generator<Pair<A, B>> =
    Generator.combine(this, other, ::Pair)

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
    combine(generator1, generator2, ::Pair)

private class CombinedGenerators<A, B, R>(
    private val generator1: Generator<A>,
    private val generator2: Generator<B>,
    private val transform: (A, B) -> R
) : Generator<R> {

    @ExperimentalKwikGeneratorApi
    override fun generateSampleTree(random: Random): SampleTree<R> =
        sampleLeaf(transform(generator1.generate(random), generator2.generate(random)))
}

/**
 * Returns a generator merging values of with the [other] generator
 */
operator fun <T> Generator<T>.plus(other: Generator<T>): Generator<T> =
    DualGenerator(this, other, 0.5)

/**
 * Returns a generator that randomly pick a value from the given list of the generator
 * according to their respective weights.
 *
 * Example:
 * ```
 * // This generator has 3/4 chances to generate a positive value and 1/4 chance to generate a negative value
 * val num = Generator.frequency(listOf(
 *     3.0 to Generator.positiveInts(),
 *     1.0 to Generator.negativeInts()
 * ))
 * ```
 *
 * @throws IllegalArgumentException
 *         if [weightedGenerators] is empty, contains negative weights or the sum of the weight is zero
 */
fun <T> Generator.Companion.frequency(
    weightedGenerators: Iterable<Pair<Double, Generator<T>>>
): Generator<T> {
    val list = weightedGenerators.asSequence()
        .onEach { (weight, _) -> require(weight >= 0.0) { "Negative weight(s) found in frequency input" } }
        .filter { (weight, _) -> weight > 0.0 }
        .sortedByDescending { (weight, _) -> weight }
        .toList()

    return when (list.size) {
        0 -> throw IllegalArgumentException("No generator (with weight > 0) to use for frequency-based generation")
        1 -> list.single().second
        2 -> list.let { (source1, source2) ->
            DualGenerator(
                source1 = source1.second,
                source2 = source2.second,
                source1Probability = source1.first / (source1.first + source2.first)
            )
        }
        else -> FrequencyGenerator(list)
    }
}

/**
 * Returns a generator that randomly pick a value from the given list of the generator
 * according to their respective weights.
 *
 * Example:
 * ```
 * // This generator has 3/4 chances to generate a positive value and 1/4 chance to generate a negative value
 * val num = Generator.frequency(
 *     3.0 to Generator.positiveInts(),
 *     1.0 to Generator.negativeInts()
 * )
 * ```
 *
 * @throws IllegalArgumentException
 *         if [weightedGenerators] is empty, contains negative weights or the sum of the weight is zero
 */
fun <T> Generator.Companion.frequency(
    vararg weightedGenerators: Pair<Double, Generator<T>>
): Generator<T> = frequency(weightedGenerators.asList())

private class DualGenerator<T>(
    private val source1: Generator<T>,
    private val source2: Generator<T>,
    private val source1Probability: Double
) : Generator<T> {

    @ExperimentalKwikGeneratorApi
    override fun generateSampleTree(random: Random): SampleTree<T> =
        if (random.nextDouble() < source1Probability) source1.generateSampleTree(random)
        else source2.generateSampleTree(random)
}

private class FrequencyGenerator<T>(private val weightedGenerators: List<Pair<Double, Generator<T>>>) : Generator<T> {
    val max = weightedGenerators.sumByDouble { (weight, _) -> weight }

    init {
        require(max > 0.0) { "The sum of the weights is zero" }
    }

    @ExperimentalKwikGeneratorApi
    override fun generateSampleTree(random: Random): SampleTree<T> {
        var value = random.nextDouble(max)

        weightedGenerators.forEach { (weight, generator) ->
            if (value < weight) return generator.generateSampleTree(random)

            value -= weight
        }

        return weightedGenerators.random(random).second.generateSampleTree(random)
    }
}
