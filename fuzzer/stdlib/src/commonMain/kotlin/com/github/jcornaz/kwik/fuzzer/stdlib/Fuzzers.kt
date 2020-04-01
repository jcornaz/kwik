package com.github.jcornaz.kwik.fuzzer.stdlib

import com.github.jcornaz.kwik.fuzzer.api.Arbitrary
import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.Fuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import com.github.jcornaz.kwik.fuzzer.api.toFuzzer
import com.github.jcornaz.kwik.fuzzer.stdlib.simplifier.int
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.stdlib.ints

/**
 * Returns a [Fuzzer] generating [Int] inputs.
 *
 * The generated inputs have high probability to be [Int.MIN_VALUE], [Int.MAX_VALUE], `-1`, `1` or `0`
 */
@ExperimentalKwikFuzzer
fun Arbitrary.int(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Fuzzer<Int> {
    val range = min..max

    return Generator.ints(min, max)
        .toFuzzer(Simplifier.int.filter { it in range })
}

@ExperimentalKwikFuzzer
fun Arbitrary.int(): Fuzzer<Int> = anyInt

@ExperimentalKwikFuzzer
private val anyInt: Fuzzer<Int> = Generator.ints().toFuzzer(Simplifier.int)
