package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi

/**
 * Find the simplest value [T] for which [satisfy] returns false.
 *
 * If there is no value for which [satisfy] returns false, then the root is returned.
 */
@ExperimentalKwikGeneratorApi
tailrec fun <T> SimplificationTree<T>.findSimplestFalsification(satisfy: (T) -> Boolean): T {
    val branchIterator = children.filterNot { satisfy(it.root) }.iterator()

    if (!branchIterator.hasNext()) return root

    return branchIterator.next().findSimplestFalsification(satisfy)
}
