package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.random.Random

/**
 * Returns a generator of [Sequence] where element count are between [minSize] and [maxSize] (inclusive)
 *
 * @param elementGen Generator to use for elements in the list
 */
fun <T> Generator.Companion.sequences(
    elementGen: Generator<T>,
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Sequence<T>> = SequenceGenerator(elementGen, minSize, maxSize)

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

private class SequenceGenerator<T>(
    private val elementGen: Generator<T>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<Sequence<T>> {
    override val samples: Set<Sequence<T>> = HashSet<Sequence<T>>().apply {
        if (minSize == 0) add(emptySequence())

        if (minSize <= 1 && maxSize >= 1) {
            elementGen.samples.forEach { add(sequenceOf(it)) }
        }
    }

    init {
        requireValidSizes(minSize, maxSize)
    }

    override fun generate(random: Random): Sequence<T> {
        val size = random.nextInt(minSize, maxSize + 1)
        val seed = random.nextLong()
        return elementGen.randomSequence(seed).take(size)
    }
}
