package com.github.jcornaz.kwik.fuzzer.api

/**
 * The shrinker type.
 *
 * A shrinker is capable of taking a value and returning a sequence of values
 * that are in some sense "smaller" than the given value.
 */
@ExperimentalKwikFuzzer
interface Shrinker<T> {

    /**
     * Returns a sequence of values that are in some sense "smaller" (or simpler) than [value].
     */
    @ExperimentalKwikFuzzer
    fun shrink(value: T): Sequence<T>
}
