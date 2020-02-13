package com.github.jcornaz.kwik.simplifier.api

/**
 * A simplifier is capable of taking a value to return a sequence of values
 * that are in some sense "simpler" than the given value.
 *
 * The definition of what "simpler" means is defined by the implementer of this interface.
 * For instance one may consider `1` simpler than `-189034789235`.
 */
@ExperimentalKwikFuzzer
interface Simplifier<T> {

    /**
     * Returns a sequence of values that are simpler than [value].
     *
     * The result sequence must be finite and should returns values ordered from simpler to more complex.
     */
    fun simplify(value: T): Sequence<T>
}

@ExperimentalKwikFuzzer
internal fun <T> simplifier(simplify: (T) -> Sequence<T>): Simplifier<T> = object : Simplifier<T> {
    override fun simplify(value: T): Sequence<T> = simplify(value)
}
