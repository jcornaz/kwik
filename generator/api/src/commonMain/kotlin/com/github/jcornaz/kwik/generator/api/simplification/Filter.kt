package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi

@ExperimentalKwikGeneratorApi
internal fun <T> SimplificationTree<T>.filter(predicate: (T) -> Boolean): SimplificationTree<T>? {
    val newChildren = children.mapNotNull { it.filter(predicate) }
    if (predicate(root)) return SimplificationTree(root, newChildren)

    val newChildrenList = newChildren.toList()

    if (newChildrenList.size < 2) return newChildren.singleOrNull()
    if (newChildrenList.size == 1) return newChildren.single()

    val lastChild = newChildrenList.last()

    return SimplificationTree(
        lastChild.root,
        newChildrenList.subList(0, newChildrenList.size - 1).asSequence() + lastChild.children
    )
}
