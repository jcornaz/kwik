package com.github.jcornaz.kwik.simplifier.api

import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class PairTest {

    @Test
    fun simplifyFirstThenSecond() {
        val first = simplifier<Int> {
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }
        val second = simplifier<Char> {
            assertEquals('Z', it)
            sequenceOf('A', 'B', 'C')
        }

        val pair = Simplifier.pair(first, second)

        assertEquals(
            expected = listOf(
                1 to 'Z',
                10 to 'A',
                2 to 'Z',
                10 to 'B',
                3 to 'Z',
                10 to 'C'
            ),
            actual = pair.simplify(10 to 'Z').toList()
        )
    }

    @Test
    fun returnEmptySequenceIfBothHaveNoSimplerValue() {
        val pair = Simplifier.pair(dontSimplify<Int>(), dontSimplify<Char>())
        assertEquals(
            expected = 0,
            actual = pair.simplify(1 to 'A').count()
        )
    }

    @Test
    fun simplifyFirstIfSecondHasNoMoreSimplerValue() {
        val first = simplifier<Int> {
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }

        val pair = Simplifier.pair(first, dontSimplify<Char>())

        assertEquals(
            expected = listOf(
                1 to 'Z',
                2 to 'Z',
                3 to 'Z'
            ),
            actual = pair.simplify(10 to 'Z').toList()
        )
    }

    @Test
    fun simplifySecondIfFirstHasNoMoreSimplerValue() {
        val second = simplifier<Int> {
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }

        val pair = Simplifier.pair(dontSimplify<Char>(), second)

        assertEquals(
            expected = listOf(
                'Z' to 1,
                'Z' to 2,
                'Z' to 3
            ),
            actual = pair.simplify('Z' to 10).toList()
        )
    }

    @Test
    fun bothAreSimplifiedWhenTryingToFindSimplestFalsification() {
        val simplifier = simplifier<Int> { value ->
            when (value) {
                0 -> emptySequence()
                1 -> sequenceOf(0)
                else -> sequenceOf(value / 2, value - 1)
            }
        }

        val result = Simplifier.pair(simplifier, simplifier)
            .findSimplestFalsification(100 to 100) { (first, _) ->
                first < 12
            }

        assertEquals(12 to 0, result)
    }
}