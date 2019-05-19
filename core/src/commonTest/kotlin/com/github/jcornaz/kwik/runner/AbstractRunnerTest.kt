package com.github.jcornaz.kwik.runner

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
}
