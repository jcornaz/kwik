package com.github.jcornaz.kwik.runner

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.forAll
import com.github.jcornaz.kwik.withSamples
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForAll4Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator.create { it.nextInt() }
    private val testGenerator2 = Generator.create { it.nextDouble() }
    private val testGenerator3 = Generator.create { it.nextLong() }
    private val testGenerator4 = Generator.create { it.nextFloat() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Boolean) {
        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, iterations, seed) { _, _, _, _ ->
            invocation()
        }
    }

    @Test
    fun evaluateSamples() {
        val ints = mutableSetOf<Int>()
        val doubles = mutableSetOf<Double>()
        val longs = mutableSetOf<Long>()
        val floats = mutableSetOf<Float>()

        val gen1 = Generator.create { it.nextInt(0, 10) }.withSamples(42, 100)
        val gen2 = Generator.create { it.nextDouble(0.0, 10.0) }.withSamples(123.0, 678.0)
        val gen3 = Generator.create { it.nextLong(0L, 10L) }.withSamples(-42L)
        val gen4 = Generator.create { it.nextDouble(0.0, 10.0).toFloat() }.withSamples(-6f, 18f)

        forAll(gen1, gen2, gen3, gen4) { i, d, l, f ->
            ints += i
            doubles += d
            longs += l
            floats += f
            true
        }

        assertTrue(42 in ints)
        assertTrue(100 in ints)
        assertTrue(123.0 in doubles)
        assertTrue(123.0 in doubles)
        assertTrue(-42L in longs)
        assertTrue(-6f in floats)
        assertTrue(18f in floats)
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()
        val valuesC = mutableSetOf<Long>()
        val valuesD = mutableSetOf<Float>()

        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, seed = 0L) { a, b, c, d ->
            valuesA += a
            valuesB += b
            valuesC += c
            valuesD += d
            true
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Input>()
        val pass2 = mutableListOf<Input>()

        val seed = 123564L

        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, seed = seed) { a, b, c, d ->
            pass1 += Input(a, b, c, d)
            true
        }

        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, seed = seed) { a, b, c, d ->
            pass2 += Input(a, b, c, d)
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { a: Int, b: Long, c: Double, d: Float ->
            a is Int && b is Long && c is Double && d is Float
        }
    }

    private data class Input(val a: Int, val b: Double, val c: Long, val d: Float)
}
