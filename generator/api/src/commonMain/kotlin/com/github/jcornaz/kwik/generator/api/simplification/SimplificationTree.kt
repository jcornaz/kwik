package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random

/**
 * Lazy tree of sample.
 *
 * The [root] is the most complex value of the tree.
 * The [branches] returns a sequences of value simpler than [root], order from the simplest to the most complex.
 */
@ExperimentalKwikGeneratorApi
class SimplificationTree<out T>(
    val root: T,
    val branches: Sequence<SimplificationTree<T>>
)

/**
 * Returns a [SimplificationTree] with only a [root] and no simpler values.
 */
@ExperimentalKwikGeneratorApi
fun <T> simplestValue(root: T): SimplificationTree<T> =
    SimplificationTree(root, emptySequence())

/**
 * Returns a [SimplificationTree] with [root] and lazily build branches by invoking [simplify]
 */
@ExperimentalKwikGeneratorApi
fun <T> simplificationTree(root: T, simplify: (T) -> Sequence<T>): SimplificationTree<T> =
    SimplificationTree(root, simplify(root).map { simplificationTree(it, simplify) })

@ExperimentalKwikGeneratorApi
fun <T> Generator<T>.withSimplification(simplify: (T) -> Sequence<T>): Generator<T> = object : Generator<T> {
    override fun generateSampleTree(random: Random): SimplificationTree<T> =
        simplificationTree(this@withSimplification.generateSampleTree(random).root, simplify)
}
