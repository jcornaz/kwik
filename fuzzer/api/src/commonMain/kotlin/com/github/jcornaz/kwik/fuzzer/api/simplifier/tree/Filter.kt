package com.github.jcornaz.kwik.fuzzer.api.simplifier.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

@ExperimentalKwikFuzzer
internal fun <T> SimplificationTree<T>.filter(predicate: (T) -> Boolean): SimplificationTree<T>? {
    if (!predicate(root)) return null

    return SimplificationTree(
        root = root,
        children = children.mapNotNull { it.filter(predicate) }
    )
}

@ExperimentalKwikFuzzer
internal fun <T, R> SimplificationTree<T>.map(transform: (T) -> R): SimplificationTree<R> =
    SimplificationTree(
        root = transform(root),
        children = children.map { it.map(transform) }
    )
