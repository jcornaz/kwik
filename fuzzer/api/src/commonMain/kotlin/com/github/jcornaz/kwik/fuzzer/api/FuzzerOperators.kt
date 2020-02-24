package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import kotlin.random.Random

/**
 * Returns a fuzzer that evaluates only elements matching the given [predicate].
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating fuzzer if possible.
 */
@ExperimentalKwikFuzzer
fun <T> Fuzzer<T>.filter(predicate: (T) -> Boolean): Fuzzer<T> =
    FilterFuzzer(this, predicate)

/**
 * Returns a fuzzer that evaluates only elements not matching the given [predicate].
 *
 * **Usage of this operator slows down the property tests**
 * Use it with caution and always favor customizing or creating fuzzer if possible.
 */
@ExperimentalKwikFuzzer
fun <T> Fuzzer<T>.filterNot(predicate: (T) -> Boolean): Fuzzer<T> =
    filter { !predicate(it) }

@ExperimentalKwikFuzzer
private class FilterFuzzer<out T>(
    private val source: Fuzzer<T>,
    private val predicate: (T) -> Boolean
) : Fuzzer<T> {
    override fun generate(random: Random): SimplificationTree<T> {
        var result: SimplificationTree<T>?

        do {
            result = source.generate(random).filter(predicate)
        } while (result == null)

        return result
    }
}
