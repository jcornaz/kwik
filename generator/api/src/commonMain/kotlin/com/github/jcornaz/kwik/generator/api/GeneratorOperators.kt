package com.github.jcornaz.kwik.generator.api

import kotlin.random.Random

private const val DEFAULT_SAMPLE_PROBABILITY = 0.2
private const val MAX_DISTINCT_TRIES = 100

/**
 * Returns a generator containing the results of applying the given transform function to each element emitted by
 * the original generator.
 */
public fun <T, R> Generator<T>.map(transform: (T) -> R): Generator<R> =
    Generator { transform(generate(it)) }

/**
 * Returns a generator that generates only elements matching the given predicate.
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating generators if possible.
 */
public fun <T> Generator<T>.filter(predicate: (T) -> Boolean): Generator<T> =
    Generator { random: Random ->
        var value = generate(random)

        while (!predicate(value))
            value = generate(random)

        value
    }

/**
 * Returns a new generator backed by [this] generator and applying the given [transform] function
 * to each element emitted by the original generator.
 *
 * Example:
 *
 * ```kotlin
 * fun listGen() = Generator.positiveInts().andThen { size -> Generator.lists<String>(size) }
 * ```
 */
public fun <T, R> Generator<T>.andThen(transform: (T) -> Generator<R>): Generator<R> =
    Generator { transform(generate(it)).generate(it) }

/**
 * Returns a generator that emits items of the upstream generator only once (based on result of `hashCode` and `equals` method)
 *
 * Be sure to use a generator function that can generate a big set of values
 * Be sure to not overuse it has it may slow down your tests
 */
public fun <T> Generator<T>.distinct(): Generator<T> {
    var generatedValues = mutableListOf<T>()
    return Generator {
        var retries = 0
        val genSize = generatedValues.size
        generatedValues.add(generate(it))
        generatedValues = generatedValues.toMutableSet().toMutableList()
        while (genSize == generatedValues.size) {
            if (retries > MAX_DISTINCT_TRIES) {
                throw Exception("Number of retries exceeded maximum. Distinct cannot be applied to this resource")
            }
            generatedValues.add(generate(it))
            generatedValues = generatedValues.toMutableSet().toMutableList()
            retries++
        }
        generatedValues.last()
    }
}

/**
 * @Deprecated Use `andThen` operator instead
 */
@Deprecated(
    "Use `andThen` operator instead",
    ReplaceWith("andThen(transform)"),
    DeprecationLevel.ERROR
)
public fun <T, R> Generator<T>.flatMap(transform: (T) -> Generator<R>): Generator<R> = andThen(transform)

/**
 * Returns a generator containing all elements except the ones matching the given predicate.
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating generators if possible.
 */
public fun <T> Generator<T>.filterNot(predicate: (T) -> Boolean): Generator<T> =
    filter { !predicate(it) }


/**
 * Returns a new generator that has a good [probability] to generate a value from the given [samples],
 * and generate from source the rest of the time.
 */
public fun <T> Generator<T>.withSamples(
    vararg samples: T,
    probability: Double = DEFAULT_SAMPLE_PROBABILITY
): Generator<T> =
    withSamples(samples.asList(), probability)

/**
 * Returns a new generator that has a good [probability] to generate a value from the given [samples],
 * and generate from source the rest of the time.
 */
public fun <T> Generator<T>.withSamples(
    samples: Iterable<T>,
    probability: Double = DEFAULT_SAMPLE_PROBABILITY
): Generator<T> {
    require(probability > 0.0 && probability < 1.0) {
        "Invalid sample probability: $probability. Must be greater than 0 and smaller than 1"
    }

    val sampleList = (samples as? List<T>) ?: samples.toList()
    if (sampleList.isEmpty()) return this

    return Generator.frequency(
        probability to Generator.of(samples),
        (1 - probability) to this
    )
}

/**
 * Returns a new generator adding `null` into generated random values.
 *
 * The "random" values always start by `null` so that it always appear at least once.
 */
public fun <T> Generator<T>.withNull(probability: Double = DEFAULT_SAMPLE_PROBABILITY): Generator<T?> =
    withSamples(listOf(null), probability)
