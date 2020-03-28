package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Returns a [Simplifier] that consider every input of [Simplifier.simplify] to be already the smallest value possible,
 * therefore always returning an empty sequence.
 *
 * As an effect, it disable simplifying.
 */
@ExperimentalKwikFuzzer
@Suppress("UNCHECKED_CAST")
fun <T> dontSimplify(): Simplifier<T> = DontSimplify()

@ExperimentalKwikFuzzer
private class DontSimplify<T> : Simplifier<T> {

    override fun tree(value: T): SimplificationTree<T> =
        simplificationTreeOf(value)
}
