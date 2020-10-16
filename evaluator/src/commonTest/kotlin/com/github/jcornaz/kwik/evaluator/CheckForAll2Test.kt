package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CheckForAll2Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator { it.nextInt() }
    private val testGenerator2 = Generator { it.nextDouble() }

    override fun evaluate(iterations: Int, seed: Long, invocation: PropertyEvaluationContext.() -> Boolean) {
        checkForAll(testGenerator1, testGenerator2, iterations, seed) { _, _ -> assertTrue(invocation()) }
    }

    @Test
    fun errorDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            checkForAll(
                generatorA = { 42 },
                generatorB = { -4.1 },
                iterations = 123, seed = 78
            ) { _, _ ->
                if (++i >= 12) error("failed")
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

        checkForAll(testGenerator1, testGenerator2, seed = 0L) { a, b ->
            valuesA += a
            valuesB += b
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Pair<Int, Double>>()
        val pass2 = mutableListOf<Pair<Int, Double>>()

        val seed = 123564L

        checkForAll(testGenerator1, testGenerator2, seed = seed) { a, b ->
            pass1 += a to b
        }

        checkForAll(testGenerator1, testGenerator2, seed = seed) { a, b ->
            pass2 += a to b
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        checkForAll { a: Int, b: Long ->
            assertTrue(a is Int && b is Long)
        }
    }
}
