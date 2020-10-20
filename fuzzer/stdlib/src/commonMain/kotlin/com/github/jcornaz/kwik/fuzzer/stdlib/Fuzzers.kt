package com.github.jcornaz.kwik.fuzzer.stdlib

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.Arbitrary
import com.github.jcornaz.kwik.fuzzer.api.Fuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import com.github.jcornaz.kwik.fuzzer.api.toFuzzer
import com.github.jcornaz.kwik.fuzzer.stdlib.simplifier.int
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.stdlib.ints

/**
 * Returns a [Fuzzer] generating [Int] inputs that are between the given [min] anx [max] values.
 *
 * The generated inputs have high probability to be [Int.MIN_VALUE], [Int.MAX_VALUE], `-1`, `1` or `0`
 */
@ExperimentalKwikApi
public fun Arbitrary.int(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Fuzzer<Int> {
    val range = min..max

    return Generator.ints(min, max)
        .toFuzzer(Simplifier.int.filter { it in range })
}

/**
 * Returns a [Fuzzer] generating [Int] inputs.
 *
 * The generated inputs have high probability to be [Int.MIN_VALUE], [Int.MAX_VALUE], `-1`, `1` or `0`
 */
@ExperimentalKwikApi
public fun Arbitrary.int(): Fuzzer<Int> = anyInt

@ExperimentalKwikApi
private val anyInt: Fuzzer<Int> by lazy { Generator.ints().toFuzzer(Simplifier.int) }
