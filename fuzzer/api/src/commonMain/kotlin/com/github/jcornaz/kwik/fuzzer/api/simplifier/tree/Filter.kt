package com.github.jcornaz.kwik.fuzzer.api.simplifier.tree

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi

@ExperimentalKwikGeneratorApi
internal fun <T> SimplificationTree<T>.filter(predicate: (T) -> Boolean): SimplificationTree<T>? {
    val newChildren = children.mapNotNull { it.filter(predicate) }
    if (predicate(root)) return SimplificationTree(
        root,
        newChildren
    )

    val newChildrenList = newChildren.toList()

    if (newChildrenList.size <= 1) return newChildren.singleOrNull()

    val lastChild = newChildrenList.last()

    return SimplificationTree(
        lastChild.root,
        newChildrenList.subList(0, newChildrenList.size - 1).asSequence() + lastChild.children
    )
}

@ExperimentalKwikGeneratorApi
internal fun <T, R> SimplificationTree<T>.map(transform: (T) -> R): SimplificationTree<R> =
    SimplificationTree(
        root = transform(root),
        children = children.map { it.map(transform) }
    )
