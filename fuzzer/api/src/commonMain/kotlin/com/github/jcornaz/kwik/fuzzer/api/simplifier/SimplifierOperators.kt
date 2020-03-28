package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Returns a simplifier that will return only values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
fun <T> Simplifier<T>.filter(predicate: (T) -> Boolean): Simplifier<T> =
    simplifier {
        this@filter.tree(it).children.map { it.item }.filter(predicate)
    }

/**
 * Returns a simplifier that will never return values matching [predicate] when simplifying.
 */
@ExperimentalKwikFuzzer
fun <T> Simplifier<T>.filterNot(predicate: (T) -> Boolean): Simplifier<T> =
    simplifier {
        this@filterNot.tree(it).children.map { it.item }.filterNot(predicate)
    }
