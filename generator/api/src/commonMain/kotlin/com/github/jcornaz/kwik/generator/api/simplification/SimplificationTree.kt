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

/**
 * Returns a [Generator] that use [simplify] to build the [SimplificationTree] of the generated values.
 */
@ExperimentalKwikGeneratorApi
fun <T> Generator<T>.withSimplification(simplify: (T) -> Sequence<T>): Generator<T> =
    Simplifier(this, simplify)

private class Simplifier<out T>(
    private val generator: Generator<T>,
    private val simplify: (T) -> Sequence<T>
): Generator<T> {

    @ExperimentalKwikGeneratorApi
    override fun generateSampleTree(random: Random): SimplificationTree<T> =
        simplificationTree(
            root = generator.generateSampleTree(random).root,
            simplify = simplify
        )
}
