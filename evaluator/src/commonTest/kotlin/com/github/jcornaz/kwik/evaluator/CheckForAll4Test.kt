package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CheckForAll4Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator { it: Random -> it.nextInt() }
    private val testGenerator2 = Generator { it: Random -> it.nextDouble() }
    private val testGenerator3 = Generator { it: Random -> it.nextLong() }
    private val testGenerator4 = Generator { it: Random -> it.nextFloat() }

    override fun evaluate(iterations: Int, seed: Long, invocation: PropertyEvaluationContext.() -> Boolean) {
        checkForAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            testGenerator4,
            iterations,
            seed
        ) { _, _, _, _ ->
            assertTrue(invocation())
        }
    }

    @Test
    fun errorDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            checkForAll(
                Generator { it: Random -> 42 },
                Generator { it: Random -> -4.1 },
                Generator { it: Random -> 100L },
                Generator { it: Random -> "hello world" },
                iterations = 123, seed = 78
            ) { _, _, _, _ ->
                if (++i >= 12) error("failed")
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

        checkForAll(
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
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Input>()
        val pass2 = mutableListOf<Input>()

        val seed = 123564L

        checkForAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            testGenerator4,
            seed = seed
        ) { a, b, c, d ->
            pass1 += Input(a, b, c, d)
        }

        checkForAll(
            testGenerator1,
            testGenerator2,
            testGenerator3,
            testGenerator4,
            seed = seed
        ) { a, b, c, d ->
            pass2 += Input(a, b, c, d)
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        checkForAll { a: Int, b: Long, c: Double, d: Float ->
            assertTrue(a is Int && b is Long && c is Double && d is Float)
        }
    }

    private data class Input(val a: Int, val b: Double, val c: Long, val d: Float)
}
