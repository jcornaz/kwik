package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.testValues
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
): Generator<List<T>> = ListGenerator(elementGen, minSize, maxSize)

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
 * Returns a generator of non-empty [List] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.nonEmptyLists(maxSize: Int = KWIK_DEFAULT_MAX_SIZE): Generator<List<T>> =
    lists(Generator.default(), 1, maxSize)

private class ListGenerator<T>(
    private val elementGen: Generator<T>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<List<T>> {
    override val samples: Set<List<T>> = mutableSetOf<List<T>>().apply {
        if (minSize == 0) add(emptyList())

        if (minSize <= 1 && maxSize >= 1) {
            elementGen.samples.forEach { add(listOf(it)) }
        }
    }

    init {
        require(minSize >= 0) { "Invalid size: $minSize" }
        require(maxSize >= minSize) { "Invalid max size: $minSize" }
    }

    override fun randoms(seed: Long): Sequence<List<T>> = sequence {
        val elements = elementGen.randoms(seed).iterator()
        val rng = Random(seed)

        while (true) {
            yield(List(rng.nextInt(minSize, maxSize + 1)) { elements.next() })
        }
    }
}

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

    override val samples: Set<Set<T>> = mutableSetOf<Set<T>>().apply {
        if (minSize == 0) add(emptySet())

        if (minSize <= 1 && maxSize >= 1) {
            elementGen.samples.forEach { add(setOf(it)) }
        }
    }

    init {
        require(minSize >= 0) { "Invalid size: $minSize" }
        require(maxSize >= minSize) { "Invalid max size: $minSize" }
    }

    override fun randoms(seed: Long): Sequence<Set<T>> = sequence {
        val elements = elementGen.randoms(seed).iterator()
        val rng = Random(seed)

        while (true) {
            val size = rng.nextInt(minSize, maxSize + 1)
            val set = HashSet<T>(size)

            repeat(size) {
                set += elements.next()
            }

            var extraAttempt = 0
            while (set.size < size && extraAttempt < MAX_EXTRA_ADD_ATTEMPT) {
                set += elements.next()
                ++extraAttempt
            }

            if (set.size < minSize) throw Exception("Failed to create a set with the requested minimum of element")

            yield(set)
        }
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

    override val samples: Set<Map<K, V>> = mutableSetOf<Map<K, V>>().apply {
        if (minSize == 0) add(emptyMap())

        if (minSize <= 1 && maxSize >= 1) {
            val values = valueGen.testValues(0).iterator()
            keyGen.samples.forEach {
                add(mapOf(it to values.next()))
            }
        }
    }

    init {
        require(minSize >= 0) { "Invalid size: $minSize" }
        require(maxSize >= minSize) { "Invalid max size: $minSize" }
    }

    override fun randoms(seed: Long): Sequence<Map<K, V>> = sequence {
        val keys = keyGen.randoms(seed).iterator()
        val values = valueGen.randoms(seed + 1).iterator()
        val rng = Random(seed)

        while (true) {
            val size = rng.nextInt(minSize, maxSize + 1)
            val map = HashMap<K, V>(size)

            repeat(size) {
                map[keys.next()] = values.next()
            }

            var extraAttempt = 0
            while (map.size < size && extraAttempt < MAX_EXTRA_ADD_ATTEMPT) {
                map[keys.next()] = values.next()
                ++extraAttempt
            }

            if (map.size < minSize) throw Exception("Failed to create a set with the requested minimum of element")

            yield(map)
        }
    }
}
