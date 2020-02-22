package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Find the simplest value [T] for which [satisfy] returns false.
 *
 * If there is no simpler value than [initialValue] for which [satisfy] returns false, then [initialValue] is returned.
 */
@ExperimentalKwikFuzzer
tailrec fun <T> Simplifier<T>.findSimplestFalsification(initialValue: T, satisfy: (T) -> Boolean): T {
    val simplerValuesIterator = simplify(initialValue).filterNot(satisfy).iterator()

    if (!simplerValuesIterator.hasNext()) return initialValue

    return findSimplestFalsification(simplerValuesIterator.next(), satisfy)
}
