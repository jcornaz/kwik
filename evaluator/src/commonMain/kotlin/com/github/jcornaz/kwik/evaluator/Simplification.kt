package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.simplifier.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.simplifier.api.Simplifier

@ExperimentalKwikFuzzer
internal tailrec fun <T> Simplifier<T>.simplify(initialValue: T, satisfy: (T) -> Boolean): T {
    val simplerValue = try {
        simplify(initialValue).first { !satisfy(it) }
    } catch (noSuchElement: NoSuchElementException) {
        return initialValue
    }

    return simplify(simplerValue, satisfy)
}
