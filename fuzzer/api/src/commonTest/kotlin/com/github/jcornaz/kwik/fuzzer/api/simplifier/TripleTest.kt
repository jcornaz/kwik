package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class TripleTest {

    @Test
    fun simplifyFirstThenSecondThenThird() {
        val first = simplifier<Int> {
            assertEquals(10, it)
            sequenceOf(1, 2)
        }
        val second = simplifier<Char> {
            assertEquals('Z', it)
            sequenceOf('A', 'B')
        }
        val third = simplifier<Double> {
            assertEquals(42.0, it)
            sequenceOf(3.14, 3.33)
        }

        val pair = Simplifier.triple(first, second, third)

        assertEquals(
            expected = listOf(
                Triple(1, 'Z', 42.0),
                Triple(10, 'A', 42.0),
                Triple(10, 'Z', 3.14),
                Triple(2, 'Z', 42.0),
                Triple(10, 'B', 42.0),
                Triple(10, 'Z', 3.33)
            ),
            actual = pair.simplify(Triple(10, 'Z', 42.0)).toList()
        )
    }


    @Test
    fun returnEmptySequenceIfNoneHaveSimplerValue() {
        val triple = Simplifier.triple(
            dontSimplify<Int>(),
            dontSimplify<Char>(),
            dontSimplify<Double>()
        )
        assertEquals(
            expected = 0,
            actual = triple.simplify(Triple(1, 'a', 0.0)).count()
        )
    }

    @Test
    fun simplifyFirstIfSecondHasNoMoreSimplerValue() {
        val first = simplifier<Int> {
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }

        val pair = Simplifier.triple(first,
            dontSimplify<Char>(),
            dontSimplify<Double>()
        )

        assertEquals(
            expected = listOf(
                Triple(1, 'Z', 42.0),
                Triple(2, 'Z', 42.0),
                Triple(3, 'Z', 42.0)
            ),
            actual = pair.simplify(Triple(10, 'Z', 42.0)).toList()
        )
    }

    @Test
    fun simplifySecondIfFirstAndThirdHaveNoMoreSimplerValue() {
        val second = simplifier<Int> {
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }

        val pair = Simplifier.triple(
            dontSimplify<Char>(), second,
            dontSimplify<Double>()
        )

        assertEquals(
            expected = listOf(
                Triple('Z', 1, 42.0),
                Triple('Z', 2, 42.0),
                Triple('Z', 3, 42.0)
            ),
            actual = pair.simplify(Triple('Z', 10, 42.0)).toList()
        )
    }

    @Test
    fun simplifyThirdIfFirstAndSecondHaveNoMoreSimplerValue() {
        val third = simplifier<Int> {
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }

        val pair = Simplifier.triple(
            dontSimplify<Double>(),
            dontSimplify<Char>(), third)

        assertEquals(
            expected = listOf(
                Triple(42.0, 'Z', 1),
                Triple(42.0, 'Z', 2),
                Triple(42.0, 'Z', 3)
            ),
            actual = pair.simplify(Triple(42.0, 'Z', 10)).toList()
        )
    }

    @Test
    fun allAreSimplifiedWhenTryingToFindSimplestFalsification() {
        val simplifier = simplifier<Int> { value ->
            when (value) {
                0 -> emptySequence()
                1 -> sequenceOf(0)
                else -> sequenceOf(value / 2, value - 1)
            }
        }

        val result = Simplifier.triple(simplifier, simplifier, simplifier)
            .findSimplestFalsification(Triple(101, 102, 103)) { (first, _, third) ->
                first < 12 || third < 42
            }

        assertEquals(Triple(12, 0, 42), result)
    }
}
