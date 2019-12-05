package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withSamples
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll1Test : AbstractRunnerTest() {

    private val testGenerator = Generator.create { it.nextInt() }

    override fun evaluate(iterations: Int, seed: Long, invocation: PropertyEvaluationContext.() -> Boolean) {
        forAll(testGenerator, iterations, seed) { invocation() }
    }

    @Test
    fun evaluateSamples() {
        val values = mutableSetOf<Int>()

        val gen = Generator.create { it.nextInt(0, 10) }.withSamples(42, 100)

        forAll<Int>(gen) {
            values += it
            true
        }

        assertTrue(42 in values)
        assertTrue(100 in values)
    }

    @Test
    fun falsificationDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            forAll<Int>(
                Generator.create { 42 },
                iterations = 123,
                seed = 78
            ) { ++i < 12 }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Generation seed: 78
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun ensureAtLeastOneCanCauseAdditionalIterations() {
        var iterations = 0

        forAll(
            Generator.create { ++iterations },
            iterations = 10
        ) { x ->
            ensureAtLeastOne { x >= 100 }
            true
        }

        assertEquals(100, iterations)
    }

    @Test
    fun multipleEnsureAtLeastOneCauseAdditionalIterationUntilTheyAreBothSatisfied() {
        var iterations = 0

        forAll(
            Generator.create { ++iterations },
            iterations = 10
        ) { x ->
            ensureAtLeastOne { x >= 100 }
            ensureAtLeastOne { x >= 10 }
            true
        }

        assertEquals(100, iterations)
    }

    @Test
    fun multipleEnsureAtLeastOneCauseAdditionalIterationUntilTheyAreBothSatisfied_orderDoesNotMatter() {
        var iterations = 0

        forAll(
            Generator.create { ++iterations },
            iterations = 10
        ) { x ->
            ensureAtLeastOne { x >= 10 }
            ensureAtLeastOne { x >= 100 }
            true
        }

        assertEquals(100, iterations)
    }

    @Test
    fun ensureAtLeastOneDoesNotCauseAdditionalIterationWhenNotNecessary() {
        var iteration = 0

        forAll(
            Generator.create { 42 },
            iterations = 123
        ) { x ->
            ++iteration
            ensureAtLeastOne { x > 10 }
            true
        }

        assertEquals(123, iteration)
    }

    @Test
    fun doesNotCauseAdditionalIterationInCaseOfFalsification() {
        var iteration = 0
        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAll(
                Generator.create { 42 },
                iterations = 123,
                seed = 78
            ) { x ->
                ++iteration
                ensureAtLeastOne { x > 10 }
                iteration < 10
            }
        }

        assertEquals(10, iteration)
        assertEquals(
            """
                Property falsified after 10 tests (out of 123)
                Argument 1: 42
                Generation seed: 78
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun errorDisplayHelpfulMessage() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            var i = 0
            forAll<Int>(
                Generator.create { 42 },
                iterations = 123,
                seed = 78
            ) {
                if (++i >= 12) error("failed")
                true
            }
        }

        assertEquals(
            """
                Property falsified after 12 tests (out of 123)
                Argument 1: 42
                Generation seed: 78
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun evaluateForRandomValues() {
        val values = mutableSetOf<Int>()

        forAll(testGenerator, seed = 0L) { it ->
            values += it
            true
        }

        assertTrue(values.size > 190)
    }

    @Test
    fun isPredictable() {
        val gen = Generator.create { it.nextInt() }

        val pass1 = mutableListOf<Int>()
        val pass2 = mutableListOf<Int>()

        val seed = 123564L

        forAll(gen, seed = seed) { it ->
            pass1 += it
            true
        }

        forAll(gen, seed = seed) { it ->
            pass2 += it
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { it: Int -> it is Int }
    }
}
