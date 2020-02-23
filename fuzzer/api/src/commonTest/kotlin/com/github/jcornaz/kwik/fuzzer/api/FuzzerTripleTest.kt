package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplifier
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class FuzzerTripleTest {

    @Test
    fun combinesGenerators() {
        val triple = Arbitrary.triple(
            Generator.of(1).toOldFuzzer(dontSimplify()),
            Generator.of(2).toOldFuzzer(dontSimplify()),
            Generator.of(3).toOldFuzzer(dontSimplify())
        )

        assertEquals(Triple(1, 2, 3), triple.generator.generate(Random))
    }

    @Test
    fun combineSimplifiers() {
        val generator = Generator.create { it.nextInt() }

        val pair = Arbitrary.triple(
            generator.toOldFuzzer(simplifier {
                sequenceOf(
                    it - 1
                )
            }),
            generator.toOldFuzzer(simplifier {
                sequenceOf(
                    it - 2
                )
            }),
            generator.toOldFuzzer(simplifier {
                sequenceOf(
                    it - 3
                )
            })
        )

        assertEquals(
            listOf(
                Triple(9, 20, 30),
                Triple(10, 18, 30),
                Triple(10, 20, 27)
            ),
            pair.simplifier.simplify(Triple(10, 20, 30)).toList()
        )
    }
}
