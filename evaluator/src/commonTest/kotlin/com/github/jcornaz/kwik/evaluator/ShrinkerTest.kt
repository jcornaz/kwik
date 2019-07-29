package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.fail

class ShrinkerTest {

    @Test
    fun returnsInitialValueIfGeneratorReturnsAnEmptySmallestListOfValue() {
        val generator = shrinker { emptyList() }

        repeat(100) {
            val initialValue = Random.nextInt()
            assertEquals(
                expected = initialValue,
                actual = generator.shrink(initialValue) { fail("property should not be evaluated") }
            )
        }
    }

    @Test
    fun returnsInitialValueIfGeneratorReturnsAllPassingValues() {
        val generator = shrinker { listOf(1, 2, 3, 4) }

        repeat(100) {
            val initialValue = Random.nextInt()
            assertEquals(
                expected = initialValue,
                actual = generator.shrink(initialValue) { true }
            )
        }
    }

    @Test
    fun findsTheSmallestFailingValue() {
        val generator = shrinker { value ->
            when (value) {
                0 -> emptyList()
                1 -> listOf(0)
                else -> listOf(value / 2, value - 1)
            }
        }

        repeat(100) {
            val initialValue = Random.nextInt(2, 1000)
            val result = generator.shrink(initialValue) { it < 2 }
            assertEquals(2, result)
        }
    }

    @Test
    fun returnsOnlyValuesThatFalsifyTheProperty() {
        val generator = shrinker { value ->
            println("shrink $value")
            when {
                value == 0 -> emptyList()
                value == -1 -> listOf(0)
                value == 1 -> listOf(0)
                value < 0 -> listOf(value / 2, value + 1)
                else -> listOf(value / 2, value - 1)
            }
        }

        repeat(1000) {
            val property: (Int) -> Boolean = { it % 2 == 0 }
            var initialValue = Random.nextInt()
            while (property(initialValue))
                initialValue = Random.nextInt()

            val result = generator.shrink(initialValue, property)
            assertFalse(property(result))
        }
    }
}

private fun shrinker(shrinkFct: (Int) -> List<Int>): Generator<Int> = object : Generator<Int> {
    override val samples: Set<Int> get() = emptySet()

    override fun randoms(seed: Long): Sequence<Int> = randomSequence(seed) { it.nextInt() }

    override fun shrink(value: Int): List<Int> = shrinkFct(value)
}
