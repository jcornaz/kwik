package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikApi
class FuzzerPairTest {

    @Test
    fun combinesGenerators() {
        val pair = Arbitrary.pair(
            Generator.of(1).toFuzzer(dontSimplify()),
            Generator.of(2).toFuzzer(dontSimplify())
        )

        assertEquals(1 to 2, pair.generator.generate(Random))
    }

    @Test
    fun combineSimplifiers() {
        val generator = Generator { it: Random -> it.nextInt() }

        val pair = Arbitrary.pair(
            generator.toFuzzer(Simplifier { it: Int ->
                sequenceOf(
                    it - 7
                )
            }),
            generator.toFuzzer(Simplifier { it: Int ->
                sequenceOf(
                    it - 8
                )
            })
        )

        assertEquals(listOf(3 to 50, 10 to 42), pair.simplifier.simplify(10 to 50).toList())
    }

    @Test
    fun canCreatePairWithSingleItemFuzzer() {
        val itemFuzz = Generator.of(1).toFuzzer { sequenceOf(it - 1) }
        assertEquals(
            Arbitrary.pair(itemFuzz).generator.generate(Random(0)),
            Arbitrary.pair(itemFuzz, itemFuzz).generator.generate(Random(0))
        )
        assertEquals(
            Arbitrary.pair(itemFuzz).simplifier.simplify(2 to 3).toList(),
            Arbitrary.pair(itemFuzz, itemFuzz).simplifier.simplify(2 to 3).toList()
        )
    }
}
