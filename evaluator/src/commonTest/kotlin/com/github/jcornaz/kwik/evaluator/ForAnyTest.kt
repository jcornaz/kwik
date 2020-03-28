package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplifier
import com.github.jcornaz.kwik.fuzzer.api.toFuzzer
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.stdlib.ints
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.fail

@ExperimentalKwikFuzzer
class ForAnyTest {

    @Test
    fun sameSeedLeadsToSameInputs() {
        val iteration1 = mutableListOf<Int>()
        val iteration2 = mutableListOf<Int>()

        forAny(Generator.ints().toFuzzer(dontSimplify()), seed = 12) {
            iteration1 += it
        }

        forAny(Generator.ints().toFuzzer(dontSimplify()), seed = 12) {
            iteration2 += it
        }

        assertEquals(iteration1, iteration2)
    }

    @Test
    fun perform200EvaluationsByDefault() {
        var invocations = 0

        forAny(Generator.ints().toFuzzer(dontSimplify())) {
            ++invocations
        }

        assertEquals(200, invocations)
    }

    @Test
    fun failFastInCaseOfFalsification() {
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
        val generator = Generator.create { it.nextInt() }

        repeat(10) {
            val seed = Random.nextLong()

            val values = mutableListOf<Int>()

            forAny(generator.toFuzzer(dontSimplify()), iterations = 100, seed = seed) {
                values += it
            }

            assertEquals(generator.randomSequence(seed).take(100).toList(), values)
        }
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
            forAny(Generator.ints().toFuzzer(dontSimplify()), iterations = 0) { }
        }
    }

    @Test
    fun simplifyInputToGetSimplerInputFalsifingTheProperty() {
        val exception = assertFailsWith<FalsifiedPropertyError> {
            forAny(
                Generator.create { 42 }
                    .toFuzzer(simplifier { value ->
                        when (value) {
                            0 -> emptySequence()
                            1 -> sequenceOf(0)
                            else -> sequenceOf(value / 2, value - 1)
                        }
                    }),
                iterations = 320,
                seed = 87
            ) {
                assertTrue(it < 10)
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
