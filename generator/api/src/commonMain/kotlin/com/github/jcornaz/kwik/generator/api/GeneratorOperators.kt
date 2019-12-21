package com.github.jcornaz.kwik.generator.api

private const val DEFAULT_SAMPLE_PROBABILITY = 0.2

/**
 * Returns a generator containing the results of applying the given transform function to each element emitted by
 * the original generator.
 */
fun <T, R> Generator<T>.map(transform: (T) -> R): Generator<R> =
    Generator.create { transform(generate(it)) }

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
fun <T, R> Generator<T>.andThen(transform: (T) -> Generator<R>): Generator<R> =
    Generator.create { generate(it).let(transform).generate(it) }

/**
 * @Deprecated Use `andThen` operator instead
 */
@Deprecated("Use `andThen` operator instead", ReplaceWith("andThen(transform)"))
fun <T, R> Generator<T>.flatMap(transform: (T) -> Generator<R>): Generator<R> = andThen(transform)

/**
 * Returns a generator that generates only elements matching the given predicate.
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating generators if possible.
 */
fun <T> Generator<T>.filter(predicate: (T) -> Boolean): Generator<T> =
    Generator.create { random ->
        var value = generate(random)

        while(!predicate(value))
            value = generate(random)

        value
    }

/**
 * Returns a generator containing all elements except the ones matching the given predicate.
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating generators if possible.
 */
fun <T> Generator<T>.filterNot(predicate: (T) -> Boolean): Generator<T> =
    filter { !predicate(it) }


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
    requireValidProbability(probability)

    val sampleList = (samples as? List<T>) ?: samples.toList()
    if (sampleList.isEmpty()) return this

    return Generator.frequency(
        probability to Generator.of(samples),
        (1 - probability) to this
    )
}

private fun requireValidProbability(probability: Double) {
    require(probability > 0.0 && probability < 1.0) {
        "Invalid sample probability: $probability. Must be greater than 0 and smaller than 1"
    }
}

/**
 * Returns a new generator adding `null` into generated random values.
 *
 * The "random" values always start by `null` so that it always appear at least once.
 */
fun <T> Generator<T>.withNull(probability: Double = DEFAULT_SAMPLE_PROBABILITY): Generator<T?> =
    withSamples(listOf(null), probability)
