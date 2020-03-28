package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Find the simplest value [T] for which [satisfy] returns false.
 *
 * If there is no value for which [satisfy] returns false, then the root is returned.
 */
@ExperimentalKwikFuzzer
tailrec fun <T> SimplificationTree<T>.findSimplestFalsification(satisfy: (T) -> Boolean): T {
    val branchIterator = children.filterNot { satisfy(it.item) }.iterator()

    if (!branchIterator.hasNext()) return item

    return branchIterator.next().findSimplestFalsification(satisfy)
}
