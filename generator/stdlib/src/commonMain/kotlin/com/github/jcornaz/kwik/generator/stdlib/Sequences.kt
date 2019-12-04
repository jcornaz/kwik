package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.andThen
import com.github.jcornaz.kwik.generator.api.randomSequence

/**
 * Returns a generator of [Sequence] where element count are between [minSize] and [maxSize] (inclusive)
 *
 * @param elementGen Generator to use for elements in the list
 */
fun <T> Generator.Companion.sequences(
    elementGen: Generator<T>,
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Sequence<T>> {
    requireValidSizes(minSize, maxSize)

    return ints(minSize, maxSize)
        .andThen { sequences(elementGen, size = it) }
}

private fun <T> Generator.Companion.sequences(
    elementGen: Generator<T>,
    size: Int
): Generator<Sequence<T>> = create { random ->
    elementGen.randomSequence(random.nextLong()).take(size)
}

/**
 * Returns a generator of non-empty [Sequence] where element count are between [minSize] and [maxSize] (inclusive)
 *
 * Use a default generator for the elements. See [Generator.Companion.default]
 */
inline fun <reified T> Generator.Companion.sequences(
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Sequence<T>> = sequences(Generator.default(), minSize, maxSize)

/**
 * Returns a generator of [Sequence] where element count are between 1 and [maxSize] (inclusive)
 *
 * @param elementGen Generator to use for elements in the list
 */
fun <T> Generator.Companion.nonEmptySequences(
    elementGen: Generator<T>,
    maxSize: Int = maxOf(1, KWIK_DEFAULT_MAX_SIZE)
): Generator<Sequence<T>> = sequences(elementGen, 1, maxSize)

/**
 * Returns a generator of non-empty [Sequence] where element count are between 1 and [maxSize] (inclusive)
 *
 * Use a default generator for the elements. See [Generator.Companion.default]
 */
inline fun <reified T> Generator.Companion.nonEmptySequences(
    maxSize: Int = maxOf(1, KWIK_DEFAULT_MAX_SIZE)
): Generator<Sequence<T>> = sequences(Generator.default(), 1, maxSize)
