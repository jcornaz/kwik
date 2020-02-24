package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Lazy simplification tree.
 *
 * It is effectively a rose-tree. (a tree with an item and children, where each child is a rose-tree)
 *
 * The [item] is the most complex value of the tree.
 * The [children] is a sequences of trees for which [item] is simpler.
 * They are ordered from the simplest to the most complex.
 */
@ExperimentalKwikFuzzer
class SimplificationTree<out T>(
    val item: T,
    val children: Sequence<SimplificationTree<T>>
)

/**
 * Returns a [SimplificationTree] with only an [item] and no children.
 */
@ExperimentalKwikFuzzer
fun <T> simplificationTreeOf(item: T): SimplificationTree<T> =
    SimplificationTree(item, emptySequence())

/**
 * Returns a [SimplificationTree] with [rootItem] and lazily build children by invoking [simplify]
 */
@ExperimentalKwikFuzzer
fun <T> simplificationTree(rootItem: T, simplify: (T) -> Sequence<T>): SimplificationTree<T> =
    SimplificationTree(
        item = rootItem,
        children = simplify(rootItem).map { simplificationTree(it, simplify) })

@ExperimentalKwikFuzzer
internal fun <T> SimplificationTree<T>.filter(predicate: (T) -> Boolean): SimplificationTree<T>? {
    if (!predicate(item)) return null

    return SimplificationTree(
        item = item,
        children = children.mapNotNull { it.filter(predicate) }
    )
}

@ExperimentalKwikFuzzer
internal fun <T, R> SimplificationTree<T>.map(transform: (T) -> R): SimplificationTree<R> =
    SimplificationTree(
        item = transform(item),
        children = children.map { it.map(transform) }
    )
