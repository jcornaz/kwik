package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi

/**
 * Lazy tree of sample.
 *
 * The [root] is the most complex value of the tree.
 * The [children] returns a sequences of value simpler than [root], ordered from the simplest to the most complex.
 */
@ExperimentalKwikGeneratorApi
class SimplificationTree<out T>(
    val root: T,
    val children: Sequence<SimplificationTree<T>>
)

/**
 * Returns a [SimplificationTree] with only a [root] and no simpler values.
 */
@ExperimentalKwikGeneratorApi
fun <T> simplestValue(root: T): SimplificationTree<T> =
    SimplificationTree(root, emptySequence())

/**
 * Returns a [SimplificationTree] with [root] and lazily build children by invoking [simplify]
 */
@ExperimentalKwikGeneratorApi
fun <T> simplificationTree(root: T, simplify: (T) -> Sequence<T>): SimplificationTree<T> =
    SimplificationTree(root, simplify(root).map { simplificationTree(it, simplify) })
