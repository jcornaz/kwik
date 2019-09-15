package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

abstract class AbstractRunnerTest {

    abstract fun evaluate(
        iterations: Int = 200,
        seed: Long = Random.nextLong(),
        invocation: PropertyEvaluationContext.() -> Boolean
    )

    @Test
    fun zeroIterationShouldFail() {
        assertFailsWith<IllegalArgumentException> {
            evaluate(iterations = 0) { true }
        }
    }

    @Test
    fun invokeThePropertyOncePerIteration() {
        var invocations = 0

        evaluate {
            invocations++
            true
        }

        assertEquals(200, invocations)
    }

    @Test
    fun failFastInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            evaluate {
                invocations++
                false
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun exceptionAreWrapped() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            evaluate {
                throw CustomException("hello from exception")
            }
        }

        assertTrue(exception.cause is CustomException)
        assertEquals("hello from exception", exception.cause!!.message)
    }

    @Test
    fun failFastInCaseOfException() {
        var invocations = 0

        assertFailsWith<FalsifiedPropertyError> {
            evaluate(iterations = 42) {
                invocations++
                throw AssertionError()
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun canSkipEvaluation() {
        Generator.create { it.nextInt() }

        val values = mutableSetOf<Int>()
        var invocations = 0
        evaluate(iterations = 321) {
            val value = Random.nextInt()
            skipIf(value % 2 == 0)

            ++invocations
            values += value
            true
        }

        assertTrue(values.none { it % 2 == 0 })
        assertEquals(321, invocations)
    }
}
