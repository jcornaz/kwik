package com.github.jcornaz.kwik.runner

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.forAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import com.github.jcornaz.kwik.withSamples
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForAll2Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator.create { it.nextInt() }
    private val testGenerator2 = Generator.create { it.nextDouble() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Boolean) {
        forAll(testGenerator1, testGenerator2, iterations, seed) { _, _ -> invocation() }
    }

    @Test
    fun evaluateSamples() {
        val ints = mutableSetOf<Int>()
        val doubles = mutableSetOf<Double>()

        val gen1 = Generator.create { it.nextInt(0, 10) }.withSamples(42, 100)
        val gen2 = Generator.create { it.nextDouble(0.0, 10.0) }.withSamples(123.0, 678.0)

        forAll(gen1, gen2) { i, d ->
            ints += i
            doubles += d
            true
        }

        assertTrue(42 in ints)
        assertTrue(100 in ints)
        assertTrue(123.0 in doubles)
        assertTrue(123.0 in doubles)
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()

        forAll(testGenerator1, testGenerator2, seed = 0L) { a, b ->
            valuesA += a
            valuesB += b
            true
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Pair<Int, Double>>()
        val pass2 = mutableListOf<Pair<Int, Double>>()

        val seed = 123564L

        forAll(testGenerator1, testGenerator2, seed = seed) { a, b ->
            pass1 += a to b
            true
        }

        forAll(testGenerator1, testGenerator2, seed = seed) { a, b ->
            pass2 += a to b
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { a: Int, b: Long ->
            a is Int && b is Long
        }
    }
}
