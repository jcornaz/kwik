package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.api.withSamples
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll1Test : AbstractRunnerTest() {

    private val testGenerator = Generator.create { it.nextInt() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Boolean) {
        forAll(testGenerator, iterations, seed) { invocation() }
    }

    @Test
    fun evaluateSamples() {
        val values = mutableSetOf<Int>()

        val gen = Generator.create { it.nextInt(0, 10) }.withSamples(42, 100)

        forAll<Int>(gen) {
            values += it
            true
        }

        assertTrue(42 in values)
        assertTrue(100 in values)
    }

    @Test
    fun falsificationDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            forAll<Int>(
                Generator.create { 42 },
                iterations = 123,
                seed = 78
            ) { ++i < 12 }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Generation seed: 78
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun evaluateForRandomValues() {
        val values = mutableSetOf<Int>()

        forAll(testGenerator, seed = 0L) { it ->
            values += it
            true
        }

        assertTrue(values.size > 190)
    }

    @Test
    fun isPredictable() {
        val gen = Generator.create { it.nextInt() }

        val pass1 = mutableListOf<Int>()
        val pass2 = mutableListOf<Int>()

        val seed = 123564L

        forAll(gen, seed = seed) { it ->
            pass1 += it
            true
        }

        forAll(gen, seed = seed) { it ->
            pass2 += it
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { it: Int -> it is Int }
    }

    @Test
    fun findsSmallestInputFalsifyingTheProperty() {
        val generator = object : Generator<Int> {
            override val samples: Set<Int>
                get() = emptySet()

            override fun randoms(seed: Long): Sequence<Int> = randomSequence(seed) { it.nextInt() }

            override fun shrink(value: Int): List<Int> = when {
                value == 0 -> emptyList()
                value == -1 || value == 1 -> listOf(0)
                value < 0 -> listOf(value / 2, value + 1)
                else -> listOf(value / 2, value - 1)
            }
        }

        repeat(100) {
            val exception = assertFailsWith<AssertionError> {
                forAll(generator) { value -> value < 1234 }
            }

            assertEquals(
                expected = "1234",
                actual = exception.message.orEmpty()
                    .substringAfter("Argument 1:")
                    .substringBefore('\n')
                    .trim()
            )
        }
    }
}
