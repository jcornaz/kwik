package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Returns a simplifier that will return only values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
fun <T> Simplifier<T>.filter(predicate: (T) -> Boolean): Simplifier<T> = object : Simplifier<T> {
    override fun simplify(value: T): Sequence<SimplificationTree<T>> =
        this@filter.simplify(value).mapNotNull { it.filter(predicate) }
}

/**
 * Returns a simplifier that will never return values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
inline fun <T> Simplifier<T>.filterNot(crossinline predicate: (T) -> Boolean): Simplifier<T> =
    filter { !predicate(it) }
