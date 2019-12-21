package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.andThen

private const val MAX_EXTRA_ADDITIONAL_ATTEMPT = 10_000

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
private fun <T> Generator.Companion.lists(
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
 * Returns a generator of non-empty [List] using a default generator for the elements
 */
inline fun <reified T> Generator.Companion.nonEmptyLists(maxSize: Int = KWIK_DEFAULT_MAX_SIZE): Generator<List<T>> =
    lists(Generator.default(), 1, maxSize)

/**
 * Returns a generator of [Set] where sizes are all between [minSize] and [maxSize] (inclusive)
 *
 * If the domain of the elements is too small,
 * this generator may fail after too many attempt to create a set of [minSize]
 *
 * @param elementGen Generator to use for elements in the set
 */
fun <T> Generator.Companion.sets(
    elementGen: Generator<T>,
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Set<T>> {
    requireValidSizes(minSize, maxSize)

    return ints(minSize, maxSize).andThen { sets(elementGen, size = it) }
}

private fun <T> Generator.Companion.sets(
    elementGen: Generator<T>,
    size: Int
): Generator<Set<T>> {
    return create { random ->
        val set = HashSet<T>(size)

        repeat(size) {
            set += elementGen.generate(random)
        }

        var extraAttempt = 0
        while (set.size < size && extraAttempt < MAX_EXTRA_ADDITIONAL_ATTEMPT) {
            set += elementGen.generate(random)
            ++extraAttempt
        }

        if (set.size < size)
            error("Failed to create a set with the requested minimum of element")

        return@create set
    }
}

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
): Generator<Map<K, V>> {
    requireValidSizes(minSize, maxSize)
    
    return ints(minSize, maxSize).andThen { maps(keyGen, valueGen, size = it) }
}

private fun <K, V> Generator.Companion.maps(
    keyGen: Generator<K>,
    valueGen: Generator<V>,
    size: Int
): Generator<Map<K, V>> = create { random ->
    val map = HashMap<K, V>(size)

    repeat(size) {
        map[keyGen.generate(random)] = valueGen.generate(random)
    }

    var extraAttempt = 0
    while (map.size < size && extraAttempt < MAX_EXTRA_ADDITIONAL_ATTEMPT) {
        map[keyGen.generate(random)] = valueGen.generate(random)
        ++extraAttempt
    }

    if (map.size < size)
        error("Failed to create a set with the requested minimum of element")

    return@create map
}

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

internal fun requireValidSizes(minSize: Int, maxSize: Int) {
    require(minSize >= 0) { "Invalid min size: $minSize" }
    require(maxSize >= minSize) { "Invalid min-max sizes: min=$maxSize max=$minSize" }
}
