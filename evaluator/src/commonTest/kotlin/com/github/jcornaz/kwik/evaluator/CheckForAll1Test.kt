package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withSamples
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CheckForAll1Test : AbstractRunnerTest() {

    private val testGenerator = Generator.create { it.nextInt() }

    override fun evaluate(iterations: Int, seed: Long, invocation: PropertyEvaluationContext.() -> Boolean) {
        checkForAll(testGenerator, iterations, seed) { assertTrue(invocation()) }
    }

    @Test
    fun evaluateSamples() {
        val values = mutableSetOf<Int>()

        val gen = Generator.create { it.nextInt(0, 10) }.withSamples(42, 100)

        checkForAll<Int>(gen) {
            values += it
        }

        assertTrue(42 in values)
        assertTrue(100 in values)
    }

    @Test
    fun falsificationDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            checkForAll<Int>(
                Generator.create { 42 },
                iterations = 123,
                seed = 78
            ) { assertTrue(++i < 12) }
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

        checkForAll<Int>(testGenerator, seed = 0L) { values += it }

        assertTrue(values.size > 190)
    }

    @Test
    fun isPredictable() {
        val gen = Generator.create { it.nextInt() }

        val pass1 = mutableListOf<Int>()
        val pass2 = mutableListOf<Int>()

        val seed = 123564L

        checkForAll<Int>(gen, seed = seed) { pass1 += it }
        checkForAll<Int>(gen, seed = seed) { pass2 += it }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        checkForAll { it: Int -> assertTrue(it is Int) }
    }
}
