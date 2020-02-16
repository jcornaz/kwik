package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.combineWith
import com.github.jcornaz.kwik.simplifier.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.simplifier.api.Simplifier
import com.github.jcornaz.kwik.simplifier.api.pair

/**
 * Returns a [Fuzzer] for pair of [A] and [B].
 *
 * @param first Fuzzer for the first elements of the pairs
 * @param second Fuzzer for the second elements of the pairs
 */
@ExperimentalKwikFuzzer
fun <A, B> Arbitrary.pair(first: Fuzzer<A>, second: Fuzzer<B>): Fuzzer<Pair<A, B>> =
    Fuzzer(
        generator = first.generator.combineWith(second.generator),
        simplifier = Simplifier.pair(first.simplifier, second.simplifier)
    )
