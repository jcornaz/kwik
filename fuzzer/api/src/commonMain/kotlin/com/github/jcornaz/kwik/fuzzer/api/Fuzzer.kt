package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator

/**
 * Definition of how to create values of type [T] that are relevant for testing.
 *
 *
 * This is the combination of
 * * a [Generator] responsible for value generation,
 * * a [Simplifier] responsbile for finding smaller/simpler values,
 * * some [guarantees] that can force the property evaluation to run as long as necessary
 *   to make sure that all guarantees are fulfilled
 */
@ExperimentalKwikFuzzer
data class Fuzzer<T>(
    val generator: Generator<T>,
    val simplifier: Simplifier<T>,
    val guarantees: List<(T) -> Boolean>
)
