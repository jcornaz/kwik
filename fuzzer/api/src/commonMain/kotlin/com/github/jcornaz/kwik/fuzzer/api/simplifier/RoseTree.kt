package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Lazy tree of sample.
 *
 * The [item] is the most complex value of the tree.
 * The [children] returns a sequences of value simpler than [item], ordered from the simplest to the most complex.
 */
@ExperimentalKwikFuzzer
class RoseTree<out T>(
    val item: T,
    val children: Sequence<RoseTree<T>>
)

/**
 * Returns a [RoseTree] with only a [item] and no simpler values.
 */
@ExperimentalKwikFuzzer
fun <T> roseTreeOf(item: T): RoseTree<T> =
    RoseTree(item, emptySequence())

/**
 * Returns a [RoseTree] with [rootItem] and lazily build children by invoking [simplify]
 */
@ExperimentalKwikFuzzer
fun <T> buildRoseTree(rootItem: T, simplify: (T) -> Sequence<T>): RoseTree<T> =
    RoseTree(
        item = rootItem,
        children = simplify(rootItem).map { buildRoseTree(it, simplify) })

@ExperimentalKwikFuzzer
internal fun <T> RoseTree<T>.filter(predicate: (T) -> Boolean): RoseTree<T>? {
    if (!predicate(item)) return null

    return RoseTree(
        item = item,
        children = children.mapNotNull { it.filter(predicate) }
    )
}

@ExperimentalKwikFuzzer
internal fun <T, R> RoseTree<T>.map(transform: (T) -> R): RoseTree<R> =
    RoseTree(
        item = transform(item),
        children = children.map { it.map(transform) }
    )
