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
    override val samples: Set<R> get() = generator1.samples.take(NUMBER_OF_SAMPLES_FOR_COMBINATION)
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

/**
 * Returns a generator that randomly pick a value from the given list of the generator according to their respective weights.
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
 * @throws IllegalArgumentException if [weightedGenerators] is empty, contains negative weights or the sum of the weight is zero
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
            DualGenerator(source1.second, source2.second, source1.first / (source1.first + source2.first))
        }
        else -> FrequencyGenerator(list)
    }
}

/**
 * Returns a generator that randomly pick a value from the given list of the generator according to their respective weights.
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
 * @throws IllegalArgumentException if [weightedGenerators] is empty, contains negative weights or the sum of the weight is zero
 */
fun <T> Generator.Companion.frequency(
    vararg weightedGenerator: Pair<Double, Generator<T>>
): Generator<T> = frequency(weightedGenerator.asList())

private class DualGenerator<T>(
    private val source1: Generator<T>,
    private val source2: Generator<T>,
    private val source1Probability: Double
) : Generator<T> {
    override val samples: Set<T> = source1.samples + source2.samples

    override fun generate(random: Random): T =
        if (random.nextDouble() < source1Probability) source1.generate(random) else source2.generate(random)
}

private class FrequencyGenerator<T>(private val weightedGenerators: List<Pair<Double, Generator<T>>>) : Generator<T> {
    override val samples: Set<T> = weightedGenerators
        .flatMapTo(HashSet()) { (_, gen) -> gen.samples }

    val max = weightedGenerators.sumByDouble { (weight, _) -> weight }

    init {
        require(max > 0.0) { "The sum of the weights is zero" }
    }

    override fun generate(random: Random): T {
        var value = random.nextDouble(max)

        weightedGenerators.forEach { (weight, generator) ->
            if (value < weight) return generator.generate(random)

            value -= weight
        }

        return weightedGenerators.random(random).second.generate(random)
    }
}
