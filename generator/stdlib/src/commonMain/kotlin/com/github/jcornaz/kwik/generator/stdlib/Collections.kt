package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.andThen
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.random.Random

private const val MAX_EXTRA_ADD_ATTEMPT = 1000

/**
 * Returns a generator of [List] where sizes are all between [minSize] and [maxSize] (inclusive)
 *
 * @param elementGen Generator to use for elements in the list
 */
fun <T> Generator.Companion.lists(
    elementGen: Generator<T>,
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<List<T>> {
    requireValidSizes(minSize, maxSize)

    return ints(min = minSize, max = maxSize)
        .andThen { lists(elementGen, size = it) }
}

/**
 * Returns a generator of [List] that of a given [size]
 *
 * @param elementGen Generator to use for elements in the list
 */
fun <T> Generator.Companion.lists(
    elementGen: Generator<T>,
    size: Int
): Generator<List<T>> {
    require(size >= 0) { "Size must be greater than 0, but was $size" }

    if (size == 0) return of(listOf(emptyList()))

    return create { random ->
        List(size) { elementGen.generate(random) }
    }
}

/**
 * Returns a generator of non-empty [List] where sizes are all between 1 and [maxSize] (inclusive)
 *
 * @param elementGen Generator to use for elements in the list
 */
fun <T> Generator.Companion.nonEmptyLists(
    elementGen: Generator<T>,
    maxSize: Int = KWIK_DEFAULT_MAX_SIZE
): Generator<List<T>> =
    lists(elementGen, 1, maxSize)

/**
 * Returns a generator of [List] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.lists(
    minSize: Int = 0,
    maxSize: Int = KWIK_DEFAULT_MAX_SIZE
): Generator<List<T>> =
    lists(Generator.default(), minSize, maxSize)

/**
 * Returns a generator of [List] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.lists(size: Int): Generator<List<T>> =
    lists(Generator.default(), size = size)

/**
 * Returns a generator of non-empty [List] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.nonEmptyLists(maxSize: Int = KWIK_DEFAULT_MAX_SIZE): Generator<List<T>> =
    lists(Generator.default(), 1, maxSize)

/**
 * Returns a generator of [Set] where sizes are all between [minSize] and [maxSize] (inclusive)
 *
 * If the domain of the elements is too small, this generator may fail after too many attempt to create a set of [minSize]
 *
 * @param elementGen Generator to use for elements in the set
 */
fun <T> Generator.Companion.sets(
    elementGen: Generator<T>,
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Set<T>> = SetGenerator(elementGen, minSize, maxSize)

/**
 * Returns a generator of non-empty [Set] where sizes are all between 1 and [maxSize] (inclusive)
 *
 * @param elementGen Generator to use for elements in the set
 */
fun <T> Generator.Companion.nonEmptySets(
    elementGen: Generator<T>,
    maxSize: Int = KWIK_DEFAULT_MAX_SIZE
): Generator<Set<T>> = sets(elementGen, 1, maxSize)

/**
 * Returns a generator of [Set] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.sets(
    minSize: Int = 0,
    maxSize: Int = KWIK_DEFAULT_MAX_SIZE
): Generator<Set<T>> =
    sets(Generator.default(), minSize, maxSize)

/**
 * Returns a generator of non-empty [Set] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.nonEmptySets(maxSize: Int = KWIK_DEFAULT_MAX_SIZE): Generator<Set<T>> =
    sets(Generator.default(), 1, maxSize)

private class SetGenerator<T>(
    private val elementGen: Generator<T>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<Set<T>> {

    override val samples: Set<Set<T>>
        get() = mutableSetOf<Set<T>>().apply {
            if (minSize == 0) add(emptySet())

            if (minSize <= 1 && maxSize >= 1) {
                elementGen.samples.forEach { add(setOf(it)) }
            }
        }

    init {
        requireValidSizes(minSize, maxSize)
    }

    override fun generate(random: Random): Set<T> {
        val size = random.nextInt(minSize, maxSize + 1)
        val set = HashSet<T>(size)

        repeat(size) {
            set += elementGen.generate(random)
        }

        var extraAttempt = 0
        while (set.size < size && extraAttempt < MAX_EXTRA_ADD_ATTEMPT) {
            set += elementGen.generate(random)
            ++extraAttempt
        }

        if (set.size < minSize)
            error("Failed to create a set with the requested minimum of element")

        return set
    }
}

/**
 * Returns a generator of [Map] where sizes are all between [minSize] and [maxSize] (inclusive)
 *
 * If the domain of the keys is too small, this generator may fail after too many attempt to create a set of [minSize]
 *
 * @param keyGen Generator to use for keys in the map
 * @param valueGen Generator to use for values in the map
 */
fun <K, V> Generator.Companion.maps(
    keyGen: Generator<K>,
    valueGen: Generator<V>,
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Map<K, V>> = MapGenerator(keyGen, valueGen, minSize, maxSize)

/**
 * Returns a generator of non-empty [Map] where sizes are all between 1 and [maxSize] (inclusive)
 *
 * @param keyGen Generator to use for keys in the map
 * @param valueGen Generator to use for values in the map
 */
fun <K, V> Generator.Companion.nonEmptyMaps(
    keyGen: Generator<K>,
    valueGen: Generator<V>,
    maxSize: Int = KWIK_DEFAULT_MAX_SIZE
): Generator<Map<K, V>> = maps(keyGen, valueGen, 1, maxSize)

/**
 * Returns a generator of [Map] using a default generator for the elements
 */
inline fun <reified K, reified V> Generator.Companion.maps(
    minSize: Int = 0,
    maxSize: Int = KWIK_DEFAULT_MAX_SIZE
): Generator<Map<K, V>> =
    maps(Generator.default(), Generator.default(), minSize, maxSize)

/**
 * Returns a generator of non-empty [Map] using a default generator for the elements
 */
inline fun <reified K, reified V> Generator.Companion.nonEmptyMaps(
    maxSize: Int = KWIK_DEFAULT_MAX_SIZE
): Generator<Map<K, V>> =
    maps(Generator.default(), Generator.default(), 1, maxSize)

private class MapGenerator<K, V>(
    private val keyGen: Generator<K>,
    private val valueGen: Generator<V>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<Map<K, V>> {

    override val samples: Set<Map<K, V>>
        get() = mutableSetOf<Map<K, V>>().apply {
            if (minSize == 0) add(emptyMap())

            if (minSize <= 1 && maxSize >= 1) {
                val values = (valueGen.samples.asSequence() + valueGen.randomSequence(0)).iterator()
                keyGen.samples.forEach {
                    add(mapOf(it to values.next()))
                }
            }
        }

    init {
        requireValidSizes(minSize, maxSize)
    }

    override fun generate(random: Random): Map<K, V> {
        val size = random.nextInt(minSize, maxSize + 1)
        val map = HashMap<K, V>(size)

        repeat(size) {
            map[keyGen.generate(random)] = valueGen.generate(random)
        }

        var extraAttempt = 0
        while (map.size < size && extraAttempt < MAX_EXTRA_ADD_ATTEMPT) {
            map[keyGen.generate(random)] = valueGen.generate(random)
            ++extraAttempt
        }

        if (map.size < minSize)
            error("Failed to create a set with the requested minimum of element")

        return map
    }
}

internal fun requireValidSizes(minSize: Int, maxSize: Int) {
    require(minSize >= 0) { "Invalid min size: $minSize" }
    require(maxSize >= minSize) { "Invalid min-max sizes: min=$maxSize max=$minSize" }
}
