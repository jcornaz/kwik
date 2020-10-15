package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.pair
import com.github.jcornaz.kwik.fuzzer.api.simplifier.triple
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.combineWith
import kotlin.random.Random

/**
 * Returns a [Fuzzer] for pair of [A] and [B].
 *
 * @param first Fuzzer for the first element of the pairs
 * @param second Fuzzer for the second element of the pairs
 */
@ExperimentalKwikApi
fun <A, B> Arbitrary.pair(first: Fuzzer<A>, second: Fuzzer<B>): Fuzzer<Pair<A, B>> =
    Fuzzer(
        generator = first.generator.combineWith(second.generator),
        simplifier = Simplifier.pair(first.simplifier, second.simplifier)
    )

/**
 * Returns a [Fuzzer] for triple of [A], [B] and [C].
 *
 * @param first Fuzzer for the first element of the pairs
 * @param second Fuzzer for the second element of the pairs
 * @param third Fuzzer for the third element of the pairs
 */
@ExperimentalKwikApi
fun <A, B, C> Arbitrary.triple(first: Fuzzer<A>, second: Fuzzer<B>, third: Fuzzer<C>): Fuzzer<Triple<A, B, C>> =
    Fuzzer(
        generator = Generator { random: Random ->
            Triple(
                first.generator.generate(random),
                second.generator.generate(random),
                third.generator.generate(random)
            )
        },
        simplifier = Simplifier.triple(first.simplifier, second.simplifier, third.simplifier)
    )
