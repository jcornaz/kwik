package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyEvalResultSpec {

    @Test
    fun alwaysTrueReturnsSatisfiedForTrue() {
        assertEquals(true.alwaysTrue(), TestResult.Satisfied)
    }

    @Test
    fun alwaysTrueReturnsFalsifiedForFalse() {
        assertEquals(false.alwaysTrue(), TestResult.Falsified(expected = "true", actual = "false"))
    }

    @Test
    fun alwaysFalseReturnsSatisfiedForFalse() {
        assertEquals(false.alwaysFalse(), TestResult.Satisfied)
    }

    @Test
    fun alwaysFalseReturnsFalsifiedForTrue() {
        assertEquals(true.alwaysFalse(), TestResult.Falsified(expected = "false", actual = "true"))
    }

    @Test
    fun neverTrueReturnsFalsifiedForTrue() {
        assertEquals(true.neverTrue(), TestResult.Falsified(expected = "false", actual = "true"))
    }

    @Test
    fun neverTrueReturnsSatisfiedForFalse() {
        assertEquals(false.neverTrue(), TestResult.Satisfied)
    }

    @Test
    fun neverFalseReturnsFalsifiedForFalse() {
        assertEquals(false.neverFalse(), TestResult.Falsified(expected = "true", actual = "false"))
    }

    @Test
    fun neverFalseReturnsSatisfiedForTrue() {
        assertEquals(true.neverFalse(), TestResult.Satisfied)
    }

    @Test
    fun alwaysEqualsReturnsSatisfiedWhenEqual() {
        assertEquals(1 alwaysEquals 1, TestResult.Satisfied)
    }

    @Test
    fun alwaysEqualsReturnsFalsifiedWhenNotEqual() {
        assertEquals(1 alwaysEquals 2, TestResult.Falsified(
            expected = "2",
            actual = "1"
        ))
    }

    @Test
    fun neverEqualsReturnsSatisfiedWhenNotEqual() {
        assertEquals(1 neverEquals 2, TestResult.Satisfied)
    }

    @Test
    fun neverEqualsReturnsFalsifiedWhenEqual() {
        assertEquals(1 neverEquals 1, TestResult.Falsified(
            expected = "NOT 1",
            actual = "1"
        ))
    }
}
