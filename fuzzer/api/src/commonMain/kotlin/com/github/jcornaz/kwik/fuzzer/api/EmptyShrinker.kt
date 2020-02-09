package com.github.jcornaz.kwik.fuzzer.api

/**
 * Returns a [Shrinker] that consider inputs of [Shrinker.shrink] to be already the smallest value possible,
 * therefore always returning an empty sequence.
 *
 * As an effect, it disable shrinking.
 */
@ExperimentalKwikFuzzer
@Suppress("UNCHECKED_CAST")
fun <T> noShrink(): Shrinker<T> = EmptyShrinker as Shrinker<T>

@ExperimentalKwikFuzzer
private object EmptyShrinker : Shrinker<Any?> {

    @ExperimentalKwikFuzzer
    override fun shrink(value: Any?): Sequence<Any?> = emptySequence()
}
