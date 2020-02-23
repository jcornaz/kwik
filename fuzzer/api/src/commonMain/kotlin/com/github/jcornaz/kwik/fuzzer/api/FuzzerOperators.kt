package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filter
import com.github.jcornaz.kwik.generator.api.filterNot
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filterNot

/**
 * Returns a [SimpleFuzzer] backed by this [Generator].
 *
 * You may pass a [simplifier]. Otherwise the resulting fuzzer will not support input simplification.
 */
@ExperimentalKwikFuzzer
fun <T> Generator<T>.toFuzzer(simplifier: Simplifier<T>): SimpleFuzzer<T> =
    SimpleFuzzer(this, simplifier)

/**
 * Force to evaluate the property has many time as necessary
 * so that the [predicate] evaluate to `true` at least once.
 */
@ExperimentalKwikFuzzer
fun <T> SimpleFuzzer<T>.ensureAtLeastOne(predicate: (T) -> Boolean): SimpleFuzzer<T> =
    copy(guarantees = guarantees + predicate)

/**
 * Returns a fuzzer that evaluates only elements matching the given [predicate].
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating fuzzer if possible.
 */
@ExperimentalKwikFuzzer
fun <T> SimpleFuzzer<T>.filter(predicate: (T) -> Boolean): SimpleFuzzer<T> =
    copy(
        generator = generator.filter(predicate),
        simplifier = simplifier.filter(predicate)
    )

/**
 * Returns a fuzzer that evaluates only elements not matching the given [predicate].
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating fuzzer if possible.
 */
@ExperimentalKwikFuzzer
fun <T> SimpleFuzzer<T>.filterNot(predicate: (T) -> Boolean): SimpleFuzzer<T> =
    copy(
        generator = generator.filterNot(predicate),
        simplifier = simplifier.filterNot(predicate)
    )
