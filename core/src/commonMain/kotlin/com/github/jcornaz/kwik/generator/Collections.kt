package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.map
import com.github.jcornaz.kwik.withSamples
import kotlin.random.Random

fun <T> Generator.Companion.lists(
    elementGen: Generator<T> = Generator.default(),
    minSize: Int = 0,
    maxSize: Int = 200
): Generator<List<T>> {
    val generator = ListGenerator(elementGen, minSize, maxSize)

    return if (minSize > 0) generator else generator.withSamples(emptyList<T>())
}

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
            yield(List(rng.nextInt(minSize, maxSize + 1)) { elements.next() })
        }
    }
}

fun <T> Generator.Companion.sets(
    elementGen: Generator<T> = Generator.default(),
    minSize: Int = 0,
    maxSize: Int = 200
): Generator<Set<T>> = lists(elementGen, minSize, maxSize).map { it.toSet() }

inline fun <reified T> Generator.Companion.sets(): Generator<Set<T>> = sets(Generator.default())
