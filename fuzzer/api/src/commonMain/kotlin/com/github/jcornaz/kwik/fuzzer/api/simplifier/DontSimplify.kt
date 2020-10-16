package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.ExperimentalKwikApi

/**
 * Returns a [Simplifier] that consider every input of [Simplifier.simplify] to be already the smallest value possible,
 * therefore always returning an empty sequence.
 *
 * As an effect, it disable simplifying.
 */
@ExperimentalKwikApi
@Suppress("UNCHECKED_CAST")
fun <T> dontSimplify(): Simplifier<T> = NoSimplifier as Simplifier<T>

@ExperimentalKwikApi
private object NoSimplifier : Simplifier<Any?> {
    override fun simplify(value: Any?): Sequence<Any?> = emptySequence()
}
