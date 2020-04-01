package com.github.jcornaz.kwik.fuzzer.stdlib

import com.github.jcornaz.kwik.fuzzer.api.Arbitrary
import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import com.github.jcornaz.kwik.fuzzer.stdlib.simplifier.int
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.stdlib.ints
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class ArbitraryIntTest {

    @Test
    fun usesIntGenerator() {
        assertEquals(
            Generator.ints(32, 54).drawSample(),
            Arbitrary.int(32, 54).generator.drawSample()
        )
    }

    @Test
    fun hasSameDefaultsThanGenerator() {
        assertEquals(
            Generator.ints().drawSample(),
            Arbitrary.int().generator.drawSample()
        )
    }

    @Test
    fun hasSameDefaultMinThanGenerator() {
        assertEquals(
            Generator.ints(max = 34).drawSample(),
            Arbitrary.int(max = 34).generator.drawSample()
        )
    }

    @Test
    fun hasSameDefaultMaxThanGenerator() {
        assertEquals(
            Generator.ints(min = 34).drawSample(),
            Arbitrary.int(min = 34).generator.drawSample()
        )
    }

    @Test
    fun doesNotHaveAnyGuarantee() {
        assertTrue(Arbitrary.int().guarantees.isEmpty())
    }

    @Test
    fun useIntSimplifier() {
        repeat(1000) {
            val initialValue = Random.nextInt()
            assertEquals(
                Simplifier.int.simplify(initialValue).toList(),
                Arbitrary.int().simplifier.simplify(initialValue).toList()
            )
        }
    }

    @Test
    fun simplifierIsFiltered() {
        Generator.create { it.nextInt(-100, 100) }
            .randomSequence(0)
            .take(1000)
            .forEach { initialValue ->
                assertEquals(
                    Simplifier.int.filter { it in 0..100 }.simplify(initialValue).toList(),
                    Arbitrary.int(0, 100).simplifier.simplify(initialValue).toList()
                )
            }
    }
}
