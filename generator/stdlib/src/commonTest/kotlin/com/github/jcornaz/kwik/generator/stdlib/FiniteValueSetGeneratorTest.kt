package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FiniteValueSetGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Int> = Generator.of(0, 1, 2, 3, 4)

    @Test
    fun failsIfNoGivenSample() {
        assertFailsWith<IllegalArgumentException> { Generator.of<Int>() }
    }

    @Test
    fun generateOnlyGivenSamples() {
        assertTrue(generator.randomSequence(-78).take(200).all { it in 0..4 })
    }

    @Test
    fun generateAllGivenSamplesWithSameProbability() {
        val counts = IntArray(5)

        generator.randomSequence(-37).take(10_000).forEach {
            counts[it]++
        }

        assertTrue(counts.all { it in 1800..2200 })
    }
}
