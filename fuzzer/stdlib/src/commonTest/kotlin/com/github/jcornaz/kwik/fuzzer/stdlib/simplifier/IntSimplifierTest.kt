package com.github.jcornaz.kwik.fuzzer.stdlib.simplifier

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.findSimplestFalsification
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filterNot
import com.github.jcornaz.kwik.generator.api.frequency
import com.github.jcornaz.kwik.generator.api.plus
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalKwikApi
class IntSimplifierTest {
    private val anyInt = Generator(Random::nextInt)
    private val anyNegativeInt = Generator { it: Random -> it.nextInt(Int.MIN_VALUE, -1) }

    @Test
    fun zeroIsTheSimplestValue() {
        assertTrue(Simplifier.int.simplify(0).none())
    }

    @Test
    fun zeroIsSimplerThanOne() {
        assertEquals(0, Simplifier.int.simplify(1).single())
    }

    @Test
    fun zeroIsSimplerThanMinusOne() {
        assertTrue(Simplifier.int.simplify(-1).any { it == 0 })
    }

    @Test
    fun positiveIsSimplerThanNegative() {
        val simplifier = Simplifier.int

        Generator.frequency(
            1.0 to Generator.of(-2, -1),
            2.0 to anyNegativeInt
        )
            .randomSequence(0)
            .take(200)
            .forEach { value ->
                assertTrue(simplifier.simplify(value).any { it == abs(value) })
            }
    }

    @Test
    fun nonZeroValueHaveSimplerValues() {
        Generator.frequency(
            1.0 to Generator.of(-2, -1, 1, 2),
            2.0 to anyInt.filterNot { it == 0 }
        )
            .randomSequence(0)
            .take(200)
            .forEach { value ->
                assertTrue(Simplifier.int.simplify(value).any())
            }
    }

    @Test
    fun simplerValuesAreCloserToZero() {
        Generator.frequency(
            1.0 to Generator.of(-2, -1, 1, 2),
            2.0 to anyInt.filterNot { it == 0 }
        )
            .randomSequence(0)
            .take(200)
            .forEach { value ->
                assertTrue(Simplifier.int.simplify(value).all {
                    (it == abs(value)) || (abs(it) < abs(value))
                })
            }
    }

    @Test
    fun returnsDistinctSequence() {
        Generator.frequency(
            2.0 to Generator.of((-2)..2),
            1.0 to anyInt
        )
            .randomSequence(0)
            .take(200)
            .forEach { value ->
                val set = HashSet<Int>()
                assertTrue(Simplifier.int.simplify(value).all(set::add))
            }
    }

    @Test
    fun allowToFindSimplerFalsifyingValue() {
        val passRange = (-42)..1337

        Generator { it: Random -> it.nextInt(Int.MIN_VALUE, passRange.first - 1) }
            .plus(Generator { it: Random -> it.nextInt(passRange.last + 1, Int.MAX_VALUE) })
            .randomSequence(0)
            .take(200)
            .forEach { initialValue ->
                val simplestValue =
                    Simplifier.int
                        .findSimplestFalsification(initialValue) { it in passRange }

                if (initialValue < 0) {
                    assertEquals(-43, simplestValue)
                } else {
                    assertEquals(1338, simplestValue)
                }
            }
    }
}
