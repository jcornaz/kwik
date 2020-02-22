package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi

/**
 * Lazy tree of sample.
 *
 * The [root] is the most complex value of the tree.
 * The [branches] returns a sequences of value simpler than [root], order from the simplest to the most complex.
 */
@ExperimentalKwikGeneratorApi
class SampleTree<out T>(
    val root: T,
    val branches: Sequence<SampleTree<T>>
)

/**
 * Returns a [SampleTree] with only a [value] as root and no simpler values.
 */
@ExperimentalKwikGeneratorApi
fun <T> sampleLeaf(value: T): SampleTree<T> =
    SampleTree(value, emptySequence())

/**
 * Returns a [SampleTree] with [value] as root, and lazily build branches by invoking [simplify]
 */
@ExperimentalKwikGeneratorApi
fun <T> sampleTree(value: T, simplify: (T) -> Sequence<T>): SampleTree<T> =
    SampleTree(value, simplify(value).map { sampleTree(it, simplify) })
