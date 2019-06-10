package com.github.jcornaz.kwik

import kotlin.random.Random

private const val SAMPLE_PROBABILITY = 0.15

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 * using the provided [transform] function applied to each pair of elements.
 */
fun <A, B, R> Generator<A>.combineWith(
    other: Generator<B>,
    transform: (A, B) -> R
): Generator<R> = CombinedGenerators(this, other, transform)

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 */
fun <A, B> Generator<A>.combineWith(
    other: Generator<B>
): Generator<Pair<A, B>> = CombinedGenerators(this, other, ::Pair)

/**
 * Returns a generator of values built from the elements of [generator1] and [generator2]
 * using the provided [transform] function applied to each pair of elements.
 */
fun <A, B, R> Generator.Companion.combine(
    generator1: Generator<A>,
    generator2: Generator<B>,
    transform: (A, B) -> R
): Generator<R> = CombinedGenerators(generator1, generator2, transform)

/**
 * Returns a generator of values built from the elements of [generator1] and [generator2]
 */
fun <A, B> Generator.Companion.combine(
    generator1: Generator<A>,
    generator2: Generator<B>
): Generator<Pair<A, B>> = CombinedGenerators(generator1, generator2, ::Pair)

private class CombinedGenerators<A, B, R>(
    private val generator1: Generator<A>,
    private val generator2: Generator<B>,
    private val transform: (A, B) -> R
) : Generator<R> {
    override val samples: Set<R> = generator1.samples.take(5)
        .flatMapTo(mutableSetOf()) { a ->
            generator2.samples.take(5).map { b -> transform(a, b) }
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
 * Returns a generator containing the results of applying the given transform function to each element emitted by
 * the original generator.
 */
fun <T, R> Generator<T>.map(transform: (T) -> R): Generator<R> = MapGenerator(this, transform)

private class MapGenerator<T, R>(private val source: Generator<T>, private val transform: (T) -> R) : Generator<R> {
    override val samples: Set<R> = source.samples.mapTo(mutableSetOf(), transform)

    override fun randoms(seed: Long): Sequence<R> = source.randoms(seed).map(transform)
}

/**
 * Returns a generator containing only elements matching the given predicate.
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating generators if possible.
 */
fun <T> Generator<T>.filter(predicate: (T) -> Boolean): Generator<T> =
    FilterGenerator(this, predicate)

/**
 * Returns a generator containing all elements except the ones matching the given predicate.
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating generators if possible.
 */
fun <T> Generator<T>.filterNot(predicate: (T) -> Boolean): Generator<T> =
    FilterGenerator(this) { !predicate(it) }

private class FilterGenerator<T>(
    private val source: Generator<T>,
    private val predicate: (T) -> Boolean
) : Generator<T> {
    override val samples: Set<T> = source.samples.filterTo(mutableSetOf(), predicate)

    override fun randoms(seed: Long): Sequence<T> = source.randoms(seed).filter(predicate)
}

/**
 * Returns a new generator adding the given [samples] into generated random values.
 *
 * The "random" values always start by the given [samples] so that they always appear at least once.
 */
fun <T> Generator<T>.withSamples(vararg samples: T): Generator<T> =
    withSamples(samples.asList())

/**
 * Returns a new generator adding the given [samples] into generated random values.
 *
 * The "random" values always start by the given [samples] so that they always appear at least once.
 */
fun <T> Generator<T>.withSamples(samples: Iterable<T>): Generator<T> {
    val sampleList = (samples as? List<T>) ?: samples.toList()
    if (sampleList.isEmpty()) return this

    return SampleGenerator(this, sampleList)
}

/**
 * Returns a new generator adding `null` into generated random values.
 *
 * The "random" values always start by `null` so that it always appear at least once.
 */
fun <T> Generator<T>.withNull(): Generator<T?> =
    NullGenerator(this)

private class SampleGenerator<T>(
    private val source: Generator<T>,
    samples: Iterable<T>
) : Generator<T> {

    override val samples: Set<T> = source.samples + samples

    init {
        require(this.samples.isNotEmpty()) { "No sample provided" }
    }

    override fun randoms(seed: Long): Sequence<T> = source.randoms(seed)
}

private class NullGenerator<T>(private val source: Generator<T>) : Generator<T?> {
    override val samples: Set<T?> = source.samples.plus<T?>(null)

    override fun randoms(seed: Long): Sequence<T?> = source.randoms(seed)
}
