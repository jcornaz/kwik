package com.github.jcornaz.kwik.runner

import com.github.jcornaz.kwik.generator.Generator
import com.github.jcornaz.kwik.forAll
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

abstract class AbstractRunnerTest {

    abstract fun evaluate(iterations: Int = 200, seed: Long = Random.nextLong(), invocation: () -> Boolean)

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
    fun exceptionAreRethrown() {
        val exception = assertFailsWith<CustomException> {
            evaluate {
                throw CustomException("hello from exception")
            }
        }

        assertEquals("hello from exception", exception.message)
    }

    @Test
    fun failFastInCaseOfException() {
        var invocations = 0

        assertFailsWith<AssertionError> {
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
        forAll(iterations = 321) { value: Int ->
            skipIf(value % 2 == 0)

            ++invocations
            values += value
            true
        }

        assertTrue(values.none { it % 2 == 0 })
        assertEquals(321, invocations)
    }
}
