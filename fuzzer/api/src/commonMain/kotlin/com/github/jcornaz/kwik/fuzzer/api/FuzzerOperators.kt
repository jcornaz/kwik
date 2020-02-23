package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filterNot
import com.github.jcornaz.kwik.generator.api.filter
import com.github.jcornaz.kwik.generator.api.filterNot

/**
 * Returns a fuzzer that evaluates only elements matching the given [predicate].
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating fuzzer if possible.
 */
@ExperimentalKwikFuzzer
fun <T> OldFuzzer<T>.filter(predicate: (T) -> Boolean): OldFuzzer<T> =
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
fun <T> OldFuzzer<T>.filterNot(predicate: (T) -> Boolean): OldFuzzer<T> =
    copy(
        generator = generator.filterNot(predicate),
        simplifier = simplifier.filterNot(predicate)
    )
