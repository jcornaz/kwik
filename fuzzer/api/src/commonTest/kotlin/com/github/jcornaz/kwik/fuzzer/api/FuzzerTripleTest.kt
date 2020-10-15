package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikApi
class FuzzerTripleTest {

    @Test
    fun combinesGenerators() {
        val triple = Arbitrary.triple(
            Generator.of(1).toFuzzer(dontSimplify()),
            Generator.of(2).toFuzzer(dontSimplify()),
            Generator.of(3).toFuzzer(dontSimplify())
        )

        assertEquals(Triple(1, 2, 3), triple.generator.generate(Random))
    }

    @Test
    fun combineSimplifiers() {
        val generator = Generator { it: Random -> it.nextInt() }

        val pair = Arbitrary.triple(
            generator.toFuzzer(Simplifier { it: Int ->
                sequenceOf(
                    it - 1
                )
            }),
            generator.toFuzzer(Simplifier { it: Int ->
                sequenceOf(
                    it - 2
                )
            }),
            generator.toFuzzer(Simplifier { it: Int ->
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
