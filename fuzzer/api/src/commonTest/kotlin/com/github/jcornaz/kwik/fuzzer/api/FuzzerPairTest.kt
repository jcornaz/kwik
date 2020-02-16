package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.simplifier.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.simplifier.api.dontSimplify
import com.github.jcornaz.kwik.simplifier.api.simplifier
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
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
        val generator = Generator.create { it.nextInt() }

        val pair = Arbitrary.pair(
            generator.toFuzzer(simplifier { sequenceOf(it - 7) }),
            generator.toFuzzer(simplifier { sequenceOf(it - 8) })
        )

        assertEquals(listOf(3 to 50, 10 to 42), pair.simplifier.simplify(10 to 50).toList())
    }
}