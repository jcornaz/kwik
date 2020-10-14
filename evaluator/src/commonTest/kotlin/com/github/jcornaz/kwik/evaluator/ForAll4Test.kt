package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll4Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator { it.nextInt() }
    private val testGenerator2 = Generator { it.nextDouble() }
    private val testGenerator3 = Generator { it.nextLong() }
    private val testGenerator4 = Generator { it.nextFloat() }

    override fun evaluate(iterations: Int, seed: Long, invocation: PropertyEvaluationContext.() -> Boolean) {
        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            testGenerator4,
            iterations,
            seed
        ) { _, _, _, _ ->
            invocation()
        }
    }

    @Test
    fun falsificationDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            forAll(
                generatorA = { 42 },
                generatorB = { -4.1 },
                generatorC = { 100L },
                generatorD = { "hello world" },
                iterations = 123, seed = 78
            ) { _, _, _, _ -> ++i < 12 }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Argument 2: -4.1
                Argument 3: 100
                Argument 4: hello world
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
                generatorC = { 100L },
                generatorD = { "hello world" },
                iterations = 123, seed = 78
            ) { _, _, _, _ ->
                if (++i >= 12) error("failed")
                true
            }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Argument 2: -4.1
                Argument 3: 100
                Argument 4: hello world
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
        val valuesD = mutableSetOf<Float>()

        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            testGenerator4,
            seed = 0L
        ) { a, b, c, d ->
            valuesA += a
            valuesB += b
            valuesC += c
            valuesD += d
            true
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Input>()
        val pass2 = mutableListOf<Input>()

        val seed = 123564L

        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            testGenerator4,
            seed = seed
        ) { a, b, c, d ->
            pass1 += Input(a, b, c, d)
            true
        }

        forAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            testGenerator4,
            seed = seed
        ) { a, b, c, d ->
            pass2 += Input(a, b, c, d)
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { a: Int, b: Long, c: Double, d: Float ->
            a is Int && b is Long && c is Double && d is Float
        }
    }

    private data class Input(val a: Int, val b: Double, val c: Long, val d: Float)
}
