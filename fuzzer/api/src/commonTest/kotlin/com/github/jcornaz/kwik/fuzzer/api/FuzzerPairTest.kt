package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplifier
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class FuzzerPairTest {

    @Test
    fun combinesGenerators() {
        val pair = Arbitrary.pair(
            Generator.of(1).toOldFuzzer(dontSimplify()),
            Generator.of(2).toOldFuzzer(dontSimplify())
        )

        assertEquals(1 to 2, pair.generator.generate(Random))
    }

    @Test
    fun combineSimplifiers() {
        val generator = Generator.create { it.nextInt() }

        val pair = Arbitrary.pair(
            generator.toOldFuzzer(simplifier {
                sequenceOf(
                    it - 7
                )
            }),
            generator.toOldFuzzer(simplifier {
                sequenceOf(
                    it - 8
                )
            })
        )

        assertEquals(listOf(3 to 50, 10 to 42), pair.simplifier.simplify(10 to 50).toList())
    }
}