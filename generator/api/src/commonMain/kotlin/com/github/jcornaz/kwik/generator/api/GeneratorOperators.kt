package com.github.jcornaz.kwik.generator.api

import kotlin.random.Random

private const val DEFAULT_SAMPLE_PROBABILITY = 0.2

/**
 * Returns a generator containing the results of applying the given transform function to each element emitted by
 * the original generator.
 */
fun <T, R> Generator<T>.map(transform: (T) -> R): Generator<R> =
    MapGenerator(this, transform)

private class MapGenerator<T, R>(private val source: Generator<T>, private val transform: (T) -> R) :
    Generator<R> {
    override val samples: Set<R> get() = source.samples.mapTo(mutableSetOf(), transform)

    override fun generate(random: Random): R = transform(source.generate(random))
}

/**
 * Returns a generator backed by the generator created when applying the given transform function to each element emitted by
 * the original generator.
 *
 * Example:
 *
 * ```kotlin
 * fun listGen() = Generator.positiveInts().andThen { size -> Generator.lists<String>(size) }
 * ```
 */
fun <T, R> Generator<T>.andThen(transform: (T) -> Generator<R>): Generator<R> =
    AndThenGenerator(this, transform)

/**
 * @Deprecated Use `andThen` operator instead
 */
@Deprecated("Use `andThen` operator instead", ReplaceWith("andThen(transform)"))
fun <T, R> Generator<T>.flatMap(transform: (T) -> Generator<R>): Generator<R> = andThen(transform)

private class AndThenGenerator<T, R>(private val source: Generator<T>, private val transform: (T) -> Generator<R>): Generator<R> {
    override val samples: Set<R>
        get() = source.samples.flatMapTo(HashSet()) { transform(it).samples }

    override fun generate(random: Random): R =
        source.generate(random).let(transform).generate(random)
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
    override val samples: Set<T> get() = source.samples.filterTo(mutableSetOf(), predicate)

    override fun generate(random: Random): T {
        var value = source.generate(random)

        while(!predicate(value))
            value = source.generate(random)

        return value
    }
}

/**
 * Returns a new generator that has a good [probability] to generate a value from the given [samples],
 * and generate from source the rest of the time.
 */
fun <T> Generator<T>.withSamples(vararg samples: T, probability: Double = DEFAULT_SAMPLE_PROBABILITY): Generator<T> =
    withSamples(samples.asList(), probability)

/**
 * Returns a new generator that has a good [probability] to generate a value from the given [samples],
 * and generate from source the rest of the time.
 */
fun <T> Generator<T>.withSamples(samples: Iterable<T>, probability: Double = DEFAULT_SAMPLE_PROBABILITY): Generator<T> {
    require(probability > 0.0 && probability < 1.0) {
        "Invalid sample probability: $probability. Must be greater than 0 and smaller than 1"
    }

    val sampleList = (samples as? List<T>) ?: samples.toList()
    if (sampleList.isEmpty()) return this

    val frequencyGen = Generator.frequency(
        probability to Generator.of(samples),
        (1 - probability) to this
    )

    return object : Generator<T> by frequencyGen {
        override val samples: Set<T>
            get() = samples.toSet()
    }
}

/**
 * Returns a new generator adding `null` into generated random values.
 *
 * The "random" values always start by `null` so that it always appear at least once.
 */
fun <T> Generator<T>.withNull(): Generator<T?> =
    NullGenerator(this)

private class NullGenerator<T>(private val source: Generator<T>) : Generator<T?> {
    override val samples: Set<T?> = source.samples.plus<T?>(null)

    override fun generate(random: Random): T? = source.generate(random)
}
