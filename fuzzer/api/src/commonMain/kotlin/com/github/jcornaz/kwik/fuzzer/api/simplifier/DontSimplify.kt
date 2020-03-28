package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * Returns a [Simplifier] that consider every input of [Simplifier.simplify] to be already the smallest value possible,
 * therefore always returning a tree without any children.
 *
 * As an effect, it disables simplifying.
 */
@ExperimentalKwikFuzzer
@Suppress("UNCHECKED_CAST")
fun <T> dontSimplify(): Simplifier<T> = DontSimplify as Simplifier<T>

@ExperimentalKwikFuzzer
private object DontSimplify : Simplifier<Any?> {

    override fun simplify(value: Any?): Sequence<SimplificationTree<Any?>> =
        emptySequence()
}
