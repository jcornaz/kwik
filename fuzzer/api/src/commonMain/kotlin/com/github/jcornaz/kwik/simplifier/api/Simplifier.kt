package com.github.jcornaz.kwik.simplifier.api

/**
 * A simplifier is capable of taking a value and returning a sequence of values
 * that are in some sense "simpler" than the given value.
 */
@ExperimentalKwikFuzzer
interface Simplifier<T> {

    /**
     * Returns a sequence of values that are simpler than [value].
     *
     * The result sequence must be finite. It can be empty.
     */
    fun simplify(value: T): Sequence<T>
}

@ExperimentalKwikFuzzer
internal fun <T> simplifier(simplify: (T) -> Sequence<T>): Simplifier<T> = object : Simplifier<T> {
    override fun simplify(value: T): Sequence<T> = simplify(value)
}
