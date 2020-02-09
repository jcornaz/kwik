package com.github.jcornaz.kwik.fuzzer

import com.github.jcornaz.kwik.generator.api.Generator

/**
 * Returns a [Fuzzer] backed by this [Generator].
 *
 * You may pass a [shrinker]. Otherwise the resulting fuzzer will not support shrinking.
 */
@ExperimentalKwikFuzzer
fun <T> Generator<T>.toFuzzer(shrinker: Shrinker<T> = noShrink()): Fuzzer<T> =
    Fuzzer(this, shrinker, emptyList())
