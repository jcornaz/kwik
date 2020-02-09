package com.github.jcornaz.kwik.fuzzer

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filter
import com.github.jcornaz.kwik.generator.api.filterNot

/**
 * Returns a [Fuzzer] backed by this [Generator].
 *
 * You may pass a [shrinker]. Otherwise the resulting fuzzer will not support shrinking.
 */
@ExperimentalKwikFuzzer
fun <T> Generator<T>.toFuzzer(shrinker: Shrinker<T> = noShrink()): Fuzzer<T> =
    Fuzzer(this, shrinker, emptyList())

/**
 * Returns a fuzzer that evaluates only elements matching the given [predicate].
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating fuzzer if possible.
 */
@ExperimentalKwikFuzzer
fun <T> Fuzzer<T>.filter(predicate: (T) -> Boolean): Fuzzer<T> =
    copy(generator.filter(predicate))

/**
 * Returns a fuzzer that evaluates only elements not matching the given [predicate].
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating fuzzer if possible.
 */
@ExperimentalKwikFuzzer
fun <T> Fuzzer<T>.filterNot(predicate: (T) -> Boolean): Fuzzer<T> =
    copy(generator.filterNot(predicate))
