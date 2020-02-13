package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.simplifier.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.simplifier.api.Simplifier

@ExperimentalKwikFuzzer
internal tailrec fun <T> Simplifier<T>.simplify(initialValue: T, satisfy: (T) -> Boolean): T {
    val simplerValuesIterator = simplify(initialValue).filterNot(satisfy).iterator()

    if (!simplerValuesIterator.hasNext()) return initialValue

    return simplify(simplerValuesIterator.next(), satisfy)
}
