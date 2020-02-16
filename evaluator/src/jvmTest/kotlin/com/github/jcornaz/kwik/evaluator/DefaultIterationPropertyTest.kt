package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.simplifier.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.toFuzzer
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.stdlib.ints
import com.github.jcornaz.kwik.simplifier.api.dontSimplify
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultIterationPropertyTest {

    @Test
    @ExperimentalKwikFuzzer
    fun forAnyUseKwikIterationsSystemPropertyByDefault() {
        var count = 0

        withSystemProperty("kwik.iterations", "101") {
            forAny(Generator.ints().toFuzzer(dontSimplify())) { _: Int ->
                ++count
            }
        }

        assertEquals(101, count)
    }

    @Test
    fun forAll1UseKwikIterationsSystemPropertyByDefault() {
        var count = 0

        withSystemProperty("kwik.iterations", "42") {
            forAll { _: Int ->
                ++count
                true
            }
        }

        assertEquals(42, count)
    }

    @Test
    fun forAll2UseKwikIterationsSystemPropertyByDefault() {
        var count = 0

        withSystemProperty("kwik.iterations", "12") {
            forAll { _: Int, _: Long ->
                ++count
                true
            }
        }

        assertEquals(12, count)
    }

    @Test
    fun forAll3UseKwikIterationsSystemPropertyByDefault() {
        var count = 0

        withSystemProperty("kwik.iterations", "36") {
            forAll { _: Int, _: Long, _: Double ->
                ++count
                true
            }
        }

        assertEquals(36, count)
    }

    @Test
    fun forAll4UseKwikIterationsSystemPropertyByDefault() {
        var count = 0

        withSystemProperty("kwik.iterations", "1") {
            forAll { _: Int, _: Long, _: Double, _: Float ->
                ++count
                true
            }
        }

        assertEquals(1, count)
    }
}


