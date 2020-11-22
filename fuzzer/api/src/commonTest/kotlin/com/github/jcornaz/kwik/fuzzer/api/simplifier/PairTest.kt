package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.ExperimentalKwikApi
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikApi
class PairTest {

    @Test
    fun simplifyFirstThenSecond() {
        val first = Simplifier { it: Int ->
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }
        val second = Simplifier { it: Char ->
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
    fun canUseSameSimplifierForBothElements() {
        val itemSimplifier = Simplifier { it: Int -> if (it > 0) sequenceOf(it - 1) else emptySequence() }

        repeat(100) {
            val initialValue = Random.nextInt(0, 5) to Random.nextInt(0, 5)
            assertEquals(
                Simplifier.pair(itemSimplifier).simplify(initialValue).toList(),
                Simplifier.pair(itemSimplifier, itemSimplifier).simplify(initialValue).toList(),
            )
        }
    }

    @Test
    fun returnEmptySequenceIfBothHaveNoSimplerValue() {
        val pair = Simplifier.pair(
            dontSimplify<Int>(),
            dontSimplify<Char>()
        )
        assertEquals(
            expected = 0,
            actual = pair.simplify(1 to 'A').count()
        )
    }

    @Test
    fun simplifyFirstIfSecondHasNoMoreSimplerValue() {
        val first = Simplifier { it: Int ->
            assertEquals(10, it)
            sequenceOf(1, 2, 3)
        }

        val pair = Simplifier.pair(first,
            dontSimplify<Char>()
        )

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
        val second = Simplifier { it: Int ->
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
        val simplifier = Simplifier { value: Int ->
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