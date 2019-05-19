package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import kotlin.random.Random

private const val MAX_EXTRA_ADD_ATTEMPT = 1000

/**
 * Returns a generator of [List] where sizes are all between [minSize] and [maxSize] (inclusive)
 *
 * @param elementGen Generator to use for elements in the list
 */
fun <T> Generator.Companion.lists(
    elementGen: Generator<T>,
    minSize: Int = DEFAULT_MIN_SIZE,
    maxSize: Int = DEFAULT_MAX_SIZE
): Generator<List<T>> = ListGenerator(elementGen, minSize, maxSize)

/**
 * Returns a generator of [List] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.lists(): Generator<List<T>> = lists(Generator.default())

private class ListGenerator<T>(
    private val elementGen: Generator<T>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<List<T>> {

    init {
        require(minSize >= 0) { "Invalid size: $minSize" }
        require(maxSize >= minSize) { "Invalid max size: $minSize" }
    }

    override fun randoms(seed: Long): Sequence<List<T>> = sequence {
        val elements = elementGen.randoms(seed).iterator()
        val rng = Random(seed)

        while (true) {
            yield(List(rng.nextSize(minSize, maxSize)) { elements.next() })
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
    minSize: Int = DEFAULT_MIN_SIZE,
    maxSize: Int = DEFAULT_MAX_SIZE
): Generator<Set<T>> = SetGenerator(elementGen, minSize, maxSize)

/**
 * Returns a generator of [Set] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.sets(): Generator<Set<T>> = sets(Generator.default())

private class SetGenerator<T>(
    private val elementGen: Generator<T>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<Set<T>> {

    init {
        require(minSize >= 0) { "Invalid size: $minSize" }
        require(maxSize >= minSize) { "Invalid max size: $minSize" }
    }

    override fun randoms(seed: Long): Sequence<Set<T>> = sequence {
        val elements = elementGen.randoms(seed).iterator()
        val rng = Random(seed)

        while (true) {
            val size = rng.nextSize(minSize, maxSize)
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
    minSize: Int = DEFAULT_MIN_SIZE,
    maxSize: Int = DEFAULT_MAX_SIZE
): Generator<Map<K, V>> = MapGenerator(keyGen, valueGen, minSize, maxSize)

/**
 * Returns a generator of [Map] using a default generator for the elements
 */
inline fun <reified K, reified V> Generator.Companion.maps(): Generator<Map<K, V>> =
    maps(Generator.default(), Generator.default())

private class MapGenerator<K, V>(
    private val keyGen: Generator<K>,
    private val valueGen: Generator<V>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<Map<K, V>> {

    init {
        require(minSize >= 0) { "Invalid size: $minSize" }
        require(maxSize >= minSize) { "Invalid max size: $minSize" }
    }

    override fun randoms(seed: Long): Sequence<Map<K, V>> = sequence {
        val keys = keyGen.randoms(seed).iterator()
        val values = valueGen.randoms(seed + 1).iterator()
        val rng = Random(seed)

        while (true) {
            val size = rng.nextSize(minSize, maxSize)
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
