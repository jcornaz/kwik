package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.stdlib.negativeInts
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class IntSimplifierTest {

    @Test
    fun zeroIsTheSimplestValue() {
        assertTrue(Simplifier.int().simplify(0).none())
    }

    @Test
    fun zeroIsSimplerThanOne() {
        assertEquals(0, Simplifier.int().simplify(1).single())
    }

    @Test
    fun zeroIsSimplerThanMinusOne() {
        assertTrue(Simplifier.int().simplify(-1).any { it == 0 })
    }

    @Test
    fun positiveIsSimplerThanNegative() {
        val simplifier = Simplifier.int()

        Generator.negativeInts()
            .randomSequence(0)
            .take(200)
            .forEach { value ->
                assertTrue(simplifier.simplify(value).any { it == abs(value) })
            }
    }

    @Test
    fun nonZeroValueHaveSimplerValues() {
        repeat(1000) {
            val value = if (Random.nextBoolean())
                Random.nextInt(Int.MIN_VALUE, -1)
            else
                Random.nextInt(1, Int.MAX_VALUE)

            assertTrue(Simplifier.int().simplify(value).any())
        }
    }

    @Test
    fun simplerValuesAreCloserToZero() {
        repeat(1000) {
            val value = if (Random.nextBoolean())
                Random.nextInt(Int.MIN_VALUE, -1)
            else
                Random.nextInt(1, Int.MAX_VALUE)

            assertTrue(Simplifier.int().simplify(value).all {
                (it == abs(value)) || (abs(it) < abs(value))
            })
        }
    }

    @Test
    fun allowToFindSimplerFalsifyingValue() {
        repeat(1000) {
            val initialValue = if (Random.nextBoolean())
                Random.nextInt(Int.MIN_VALUE, -1338)
            else
                Random.nextInt(43, Int.MAX_VALUE)

            val simplerValue = Simplifier.int().findSimplestFalsification(initialValue) {
                it in (-1337)..42
            }

            assertTrue(simplerValue == 43 || simplerValue == -1338)
        }
    }
}
