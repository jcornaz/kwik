package com.github.jcornaz.kwik.simplifier.api

/**
 * Returns a simplifier that will return only values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
fun <T> Simplifier<T>.filter(predicate: (T) -> Boolean): Simplifier<T> = simplifier {
    this@filter.simplify(it).filter(predicate)
}

/**
 * Returns a simplifier that will never return values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
fun <T> Simplifier<T>.filterNot(predicate: (T) -> Boolean): Simplifier<T> = simplifier {
    this@filterNot.simplify(it).filterNot(predicate)
}
