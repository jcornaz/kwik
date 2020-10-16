package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll2Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator { it.nextInt() }
    private val testGenerator2 = Generator { it.nextDouble() }

    override fun evaluate(iterations: Int, seed: Long, invocation: PropertyEvaluationContext.() -> Boolean) {
        forAll(
            testGenerator1,
            testGenerator2,
            iterations,
            seed
        ) { _, _ -> invocation() }
    }

    @Test
    fun falsificationDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            forAll(
                generatorA = { 42 },
                generatorB = { -4.1 },
                iterations = 123, seed = 78
            ) { _, _ -> ++i < 12 }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Argument 2: -4.1
                Generation seed: 78
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun errorDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            forAll(
                generatorA = { 42 },
                generatorB = { -4.1 },
                iterations = 123, seed = 78
            ) { _, _ ->
                if (++i >= 12) error("failed")
                true
            }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Argument 2: -4.1
                Generation seed: 78
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()

        forAll(testGenerator1, testGenerator2, seed = 0L) { a, b ->
            valuesA += a
            valuesB += b
            true
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Pair<Int, Double>>()
        val pass2 = mutableListOf<Pair<Int, Double>>()

        val seed = 123564L

        forAll(testGenerator1, testGenerator2, seed = seed) { a, b ->
            pass1 += a to b
            true
        }

        forAll(testGenerator1, testGenerator2, seed = seed) { a, b ->
            pass2 += a to b
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { a: Int, b: Long ->
            a is Int && b is Long
        }
    }
}
