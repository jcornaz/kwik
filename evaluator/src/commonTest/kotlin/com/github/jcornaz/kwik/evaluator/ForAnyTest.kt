package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.simplifier.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.ensureAtLeastOne
import com.github.jcornaz.kwik.fuzzer.api.toFuzzer
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.stdlib.ints
import kotlin.random.Random
import kotlin.test.*

@ExperimentalKwikFuzzer
class ForAnyTest {

    @Test
    fun perform200EvaluationsByDefault() {
        var invocations = 0

        forAny(Generator.ints().toFuzzer()) {
            ++invocations
        }

        assertEquals(200, invocations)
    }

    @Test
    fun failFastInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            forAny(Generator.ints().toFuzzer()) {
                ++invocations
                fail()
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun takeInputsFromGenerator() {
        val generator = Generator.create { it.nextInt() }

        repeat(10) {
            val seed = Random.nextLong()

            val values = mutableListOf<Int>()

            forAny(generator.toFuzzer(), iterations = 100, seed = seed) {
                values += it
            }

            assertEquals(generator.randomSequence(seed).take(100).toList(), values)
        }
    }

    @Test
    fun wrapErrorIntoFalsifiedPropertyError() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(Generator.of(12).toFuzzer(), iterations = 42, seed = 24) {
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
            forAny(Generator.ints().toFuzzer(), iterations = 0) { }
        }
    }

    @Test
    fun guaranteesCanCauseAdditionalEvaluation() {
        var iterations = 0

        forAny(
            Generator.create { iterations + 1 }
                .toFuzzer()
                .ensureAtLeastOne { it >= 100 },
            iterations = 10
        ) { ++iterations }

        assertEquals(100, iterations)
    }


    @Test
    fun multipleGuaranteesCauseAdditionalIterationUntilTheyAreAllSatisfied() {
        var iterations = 0

        forAny(
            Generator.create { iterations + 1 }
                .toFuzzer()
                .ensureAtLeastOne { it >= 100 }
                .ensureAtLeastOne { it >= 10 },
            iterations = 10
        ) { ++iterations }

        assertEquals(100, iterations)
    }

    @Test
    fun multipleGuaranteesCauseAdditionalIterationUntilTheyAreBothSatisfied_orderDoesNotMatter() {
        var iterations = 0

        forAny(
            Generator.create { iterations + 1 }
                .toFuzzer()
                .ensureAtLeastOne { it >= 10 }
                .ensureAtLeastOne { it >= 100 },
            iterations = 10
        ) { ++iterations }

        assertEquals(100, iterations)
    }

    @Test
    fun guaranteesDoesNotCauseAdditionalIterationWhenNotNecessary() {
        var iteration = 0

        forAny(
            Generator.create { 42 }
                .toFuzzer()
                .ensureAtLeastOne { it > 10 }
            ,
            iterations = 123
        ) { ++iteration }

        assertEquals(123, iteration)
    }

    @Test
    fun doesNotCauseAdditionalIterationInCaseOfFalsification() {
        var iteration = 0

        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(
                Generator.create { 42 }
                    .toFuzzer()
                    .ensureAtLeastOne { it > 10 },
                iterations = 123,
                seed = 78
            ) {
                ++iteration
                assertTrue(iteration < 10)
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
}
