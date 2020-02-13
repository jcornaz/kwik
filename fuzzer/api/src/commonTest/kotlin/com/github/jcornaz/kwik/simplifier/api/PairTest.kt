package com.github.jcornaz.kwik.simplifier.api

import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class PairTest {

    @Test
    fun alternatesSimplestValuesOfFirstAndSecond() {
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
}