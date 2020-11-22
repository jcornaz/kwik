package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.TestResult
import com.github.jcornaz.kwik.alwaysTrue
import com.github.jcornaz.kwik.fuzzer.api.ensureAtLeastOne
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.fuzzer.api.toFuzzer
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.stdlib.ints
import kotlin.random.Random
import kotlin.test.*

@ExperimentalKwikApi
class ForAnyTest {

    @Test
    fun perform200EvaluationsByDefault() {
        var invocations = 0

        forAny(Generator.ints().toFuzzer(dontSimplify())) {
            ++invocations
            TestResult.Satisfied
        }

        assertEquals(200, invocations)
    }

    @Test
    fun failsInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            forAny(Generator.ints().toFuzzer(dontSimplify())) {
                ++invocations
                TestResult.Falsified("", "")
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun failsFastInCaseOfThrow() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            forAny(Generator.ints().toFuzzer(dontSimplify())) {
                ++invocations
                fail()
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun takeInputsFromGenerator() {
        val generator = Generator { it.nextInt() }

        repeat(10) {
            val seed = Random.nextLong()

            val values = mutableListOf<Int>()

            forAny(generator.toFuzzer(dontSimplify()), iterations = 100, seed = seed) {
                values += it
                TestResult.Satisfied
            }

            assertEquals(generator.randomSequence(seed).take(100).toList(), values)
        }
    }

    @Test
    fun throwFalsifiedPropertyErrorWithInfoInCaseOfFalsification() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(Generator.of(12).toFuzzer(dontSimplify()), iterations = 42, seed = 24) {
                TestResult.Falsified(expected = "something", actual = "something else")
            }
        }

        assertEquals(
            """
                Property falsified after 1 tests (out of 42)
                Argument 1: 12
                Generation seed: 24
                Expected: something
                Actual: something else
            """.trimIndent(),
            exception.message
        )

        assertNull(exception.cause)
    }

    @Test
    fun wrapErrorIntoFalsifiedPropertyError() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(Generator.of(12).toFuzzer(dontSimplify()), iterations = 42, seed = 24) {
                throw CustomException("my message")
            }
        }

        assertEquals(
            """
                Property falsified after 1 tests (out of 42)
                Argument 1: 12
                Generation seed: 24
            """.trimIndent(),
            exception.message
        )

        exception.cause.let { cause ->
            assertTrue(cause is CustomException)
            assertEquals("my message", cause.message)
        }
    }

    @Test
    fun failsForZeroIteration() {
        assertFailsWith<IllegalArgumentException> {
            forAny(Generator.ints().toFuzzer(dontSimplify()), iterations = 0) { TestResult.Satisfied }
        }
    }

    @Test
    fun guaranteesCanCauseAdditionalEvaluation() {
        var iterations = 0

        forAny(
            Generator { iterations + 1 }
                .toFuzzer(dontSimplify())
                .ensureAtLeastOne { it >= 100 },
            iterations = 10
        ) { ++iterations ; TestResult.Satisfied }

        assertEquals(100, iterations)
    }

    @Test
    fun skipTestResultCausesAdditionalEvaluation() {
        var iterations = 0

        forAny(
            Generator { iterations + 1 }.toFuzzer(dontSimplify()),
            iterations = 10
        ) { if (++iterations % 2 != 0) TestResult.Skip else TestResult.Satisfied }

        assertEquals(20, iterations)
    }

    @Test
    fun multipleGuaranteesCauseAdditionalIterationUntilTheyAreAllSatisfied() {
        var iterations = 0

        forAny(
            Generator { iterations + 1 }
                .toFuzzer(dontSimplify())
                .ensureAtLeastOne { it >= 100 }
                .ensureAtLeastOne { it >= 10 },
            iterations = 10
        ) { ++iterations ; TestResult.Satisfied }

        assertEquals(100, iterations)
    }

    @Test
    fun multipleGuaranteesCauseAdditionalIterationUntilTheyAreBothSatisfied_orderDoesNotMatter() {
        var iterations = 0

        forAny(
            Generator { iterations + 1 }
                .toFuzzer(dontSimplify())
                .ensureAtLeastOne { it >= 10 }
                .ensureAtLeastOne { it >= 100 },
            iterations = 10
        ) { ++iterations ; TestResult.Satisfied }

        assertEquals(100, iterations)
    }

    @Test
    fun guaranteesDoesNotCauseAdditionalIterationWhenNotNecessary() {
        var iteration = 0

        forAny(
            Generator { 42 }
                .toFuzzer(dontSimplify())
                .ensureAtLeastOne { it > 10 },
            iterations = 123
        ) { ++iteration ; TestResult.Satisfied }

        assertEquals(123, iteration)
    }

    @Test
    @Ignore
    fun doesNotCauseAdditionalIterationInCaseOfFalsification() {
        var iteration = 0

        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(
                Generator { 42 }
                    .toFuzzer(dontSimplify())
                    .ensureAtLeastOne { it > 10 },
                iterations = 123,
                seed = 78
            ) {
                ++iteration
                (iteration < 10).alwaysTrue()
            }
        }

        assertEquals(10, iteration)
        assertEquals(
            """
                Property falsified after 10 tests (out of 123)
                Argument 1: 42
                Generation seed: 78
                Expected: true
                Actual: false
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun doesNotCauseAdditionalIterationInCaseOfError() {
        var iteration = 0

        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(
                Generator { 42 }
                    .toFuzzer(dontSimplify())
                    .ensureAtLeastOne { it > 10 },
                iterations = 123,
                seed = 78
            ) {
                ++iteration
                assertTrue(iteration < 10)
                TestResult.Satisfied
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
    @Ignore
    fun simplifyInputToGetSimplerInputFalsifingTheProperty() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(
                Generator { 42 }
                    .toFuzzer { value ->
                        when (value) {
                            0 -> emptySequence()
                            1 -> sequenceOf(0)
                            else -> sequenceOf(value / 2, value - 1)
                        }
                    },
                iterations = 320,
                seed = 87
            ) {
                assertTrue(it < 10)
                TestResult.Satisfied
            }
        }

        assertEquals(
            """
                Property falsified after 1 tests (out of 320)
                Argument 1: 10
                Generation seed: 87
            """.trimIndent(),
            exception.message
        )
    }

    @Test
    fun simplifyInputToGetSimplerInputFalsifingThePropertyOnError() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(
                Generator { 42 }
                    .toFuzzer { value ->
                        when (value) {
                            0 -> emptySequence()
                            1 -> sequenceOf(0)
                            else -> sequenceOf(value / 2, value - 1)
                        }
                    },
                iterations = 320,
                seed = 87
            ) {
                assertTrue(it < 10)
                TestResult.Satisfied
            }
        }

        assertEquals(
            """
                Property falsified after 1 tests (out of 320)
                Argument 1: 10
                Generation seed: 87
            """.trimIndent(),
            exception.message
        )
    }
}
