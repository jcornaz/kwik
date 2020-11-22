package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyEvalResultSpec {

    @Test
    fun alwaysTrueReturnsSatisfiedForTrue() {
        assertEquals(true.alwaysTrue(), PropertyEvalResult.Satisfied)
    }

    @Test
    fun alwaysTrueReturnsFalsifiedForFalse() {
        assertEquals(false.alwaysTrue(), PropertyEvalResult.Falsified(expected = "true", actual = "false"))
    }

    @Test
    fun alwaysFalseReturnsSatisfiedForFalse() {
        assertEquals(false.alwaysFalse(), PropertyEvalResult.Satisfied)
    }

    @Test
    fun alwaysFalseReturnsFalsifiedForTrue() {
        assertEquals(true.alwaysFalse(), PropertyEvalResult.Falsified(expected = "false", actual = "true"))
    }

    @Test
    fun neverTrueReturnsFalsifiedForTrue() {
        assertEquals(true.neverTrue(), PropertyEvalResult.Falsified(expected = "false", actual = "true"))
    }

    @Test
    fun neverTrueReturnsSatisfiedForFalse() {
        assertEquals(false.neverTrue(), PropertyEvalResult.Satisfied)
    }

    @Test
    fun neverFalseReturnsFalsifiedForFalse() {
        assertEquals(false.neverFalse(), PropertyEvalResult.Falsified(expected = "true", actual = "false"))
    }

    @Test
    fun neverFalseReturnsSatisfiedForTrue() {
        assertEquals(true.neverFalse(), PropertyEvalResult.Satisfied)
    }

    @Test
    fun alwaysEqualsReturnsSatisfiedWhenEqual() {
        assertEquals(1 alwaysEquals 1, PropertyEvalResult.Satisfied)
    }

    @Test
    fun alwaysEqualsReturnsFalsifiedWhenNotEqual() {
        assertEquals(1 alwaysEquals 2, PropertyEvalResult.Falsified(
            expected = "2",
            actual = "1"
        ))
    }

    @Test
    fun neverEqualsReturnsSatisfiedWhenNotEqual() {
        assertEquals(1 neverEquals 2, PropertyEvalResult.Satisfied)
    }

    @Test
    fun neverEqualsReturnsFalsifiedWhenEqual() {
        assertEquals(1 neverEquals 1, PropertyEvalResult.Falsified(
            expected = "NOT 1",
            actual = "1"
        ))
    }
}
