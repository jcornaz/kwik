package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Returns a simplifier that will return only values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
fun <T> Simplifier<T>.filter(predicate: (T) -> Boolean): Simplifier<T> = object : Simplifier<T> {
    override fun simplify(value: T): Sequence<SimplificationTree<T>> =
        this@filter.simplify(value).filter { predicate(it.item) }
}

/**
 * Returns a simplifier that will never return values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
fun <T> Simplifier<T>.filterNot(predicate: (T) -> Boolean): Simplifier<T> =
    filter { !predicate(it) }
