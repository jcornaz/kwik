package com.github.jcornaz.kwik.fuzzer.api

/**
 * Returns a [Simplifier] that consider every input of [Simplifier.simplify] to be already the smallest value possible,
 * therefore always returning an empty sequence.
 *
 * As an effect, it disable simplifying.
 */
@ExperimentalKwikFuzzer
@Suppress("UNCHECKED_CAST")
fun <T> dontSimplify(): Simplifier<T> = NoSimplifier as Simplifier<T>

@ExperimentalKwikFuzzer
private object NoSimplifier : Simplifier<Any?> {

    @ExperimentalKwikFuzzer
    override fun simplify(value: Any?): Sequence<Any?> = emptySequence()
}
