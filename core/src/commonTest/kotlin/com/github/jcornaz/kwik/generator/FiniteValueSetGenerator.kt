package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FiniteValueSetGenerator : AbstractGeneratorTest() {
    override val generator: Generator<Int> = Generator.of(0, 1, 2, 3, 4)

    @Test
    fun failsIfNoGivenSample() {
        assertFailsWith<IllegalArgumentException> { Generator.of<Int>() }
    }

    @Test
    fun generateOnlyGivenSamples() {
        assertTrue(generator.randoms(-78).take(200).all { it in 0..4 })
    }

    @Test
    fun generateAllGivenSamplesWithSameProbability() {
        val counts = IntArray(5)

        generator.randoms(-37).take(10_000).forEach {
            counts[it]++
        }

        assertTrue(counts.all { it in 1800..2200 })
    }

    @Test
    fun provideNoSamples() {
        assertTrue(generator.samples.isEmpty())
    }
}
