package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.ExperimentalKwikApi

/**
 * Returns a simplifier that will return only values matching [predicate] when simplifying.
 */
@ExperimentalKwikApi
fun <T> Simplifier<T>.filter(predicate: (T) -> Boolean): Simplifier<T> =
    Simplifier { it: T ->
        this@filter.simplify(it).filter(predicate)
    }

/**
 * Returns a simplifier that will never return values matching [predicate] when simplifying.
 */
@ExperimentalKwikApi
fun <T> Simplifier<T>.filterNot(predicate: (T) -> Boolean): Simplifier<T> =
    Simplifier { it: T ->
        this@filterNot.simplify(it).filterNot(predicate)
    }
