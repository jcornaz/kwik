package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.combineWith
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.pair
import com.github.jcornaz.kwik.fuzzer.api.simplifier.triple

/**
 * Returns a [OldFuzzer] for pair of [A] and [B].
 *
 * @param first Fuzzer for the first element of the pairs
 * @param second Fuzzer for the second element of the pairs
 */
@ExperimentalKwikFuzzer
fun <A, B> Arbitrary.pair(first: OldFuzzer<A>, second: OldFuzzer<B>): OldFuzzer<Pair<A, B>> =
    OldFuzzer(
        generator = first.generator.combineWith(second.generator),
        simplifier = Simplifier.pair(first.simplifier, second.simplifier)
    )

/**
 * Returns a [OldFuzzer] for triple of [A], [B] and [C].
 *
 * @param first Fuzzer for the first element of the pairs
 * @param second Fuzzer for the second element of the pairs
 * @param third Fuzzer for the third element of the pairs
 */
@ExperimentalKwikFuzzer
fun <A, B, C> Arbitrary.triple(first: OldFuzzer<A>, second: OldFuzzer<B>, third: OldFuzzer<C>): OldFuzzer<Triple<A, B, C>> =
    OldFuzzer(
        generator = Generator.create { random ->
            Triple(
                first.generator.generate(random),
                second.generator.generate(random),
                third.generator.generate(random)
            )
        },
        simplifier = Simplifier.triple(first.simplifier, second.simplifier, third.simplifier)
    )
