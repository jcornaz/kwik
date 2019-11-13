package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll3Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator.create { it.nextInt() }
    private val testGenerator2 = Generator.create { it.nextDouble() }
    private val testGenerator3 = Generator.create { it.nextLong() }

    override fun evaluate(iterations: Int, seed: Long, invocation: PropertyEvaluationContext.() -> Boolean) {
        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            iterations,
            seed
        ) { _, _, _ -> invocation() }
    }

    @Test
    fun falsificationDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            forAll(
                Generator.create { 42 },
                Generator.create { -4.1 },
                Generator.create { 100L },
                iterations = 123, seed = 78
            ) { _, _, _ -> ++i < 12 }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Argument 2: -4.1
                Argument 3: 100
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
                Generator.create { 42 },
                Generator.create { -4.1 },
                Generator.create { 100L },
                iterations = 123, seed = 78
            ) { _, _, _ ->
                if (++i >= 12 ) error("failed")
                true
            }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Argument 2: -4.1
                Argument 3: 100
                Generation seed: 78
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()
        val valuesC = mutableSetOf<Long>()

        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            seed = 0L
        ) { a, b, c ->
            valuesA += a
            valuesB += b
            valuesC += c
            true
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Triple<Int, Double, Long>>()
        val pass2 = mutableListOf<Triple<Int, Double, Long>>()

        val seed = 123564L

        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            seed = seed
        ) { a, b, c ->
            pass1 += Triple(a, b, c)
            true
        }

        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            seed = seed
        ) { a, b, c ->
            pass2 += Triple(a, b, c)
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { a: Int, b: Long, c: Double ->
            a is Int && b is Long && c is Double
        }
    }
}
