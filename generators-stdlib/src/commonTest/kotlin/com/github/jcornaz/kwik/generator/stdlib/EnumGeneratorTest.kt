package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.Generator
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class EnumGeneratorTest : AbstractGeneratorTest() {
    enum class ExampleEnum { A, B, C, D, E }
    enum class EmptyEnum

    override val generator: Generator<ExampleEnum> = Generator.enum()

    @Test
    fun failsIfNoGivenSample() {
        assertFailsWith<IllegalArgumentException> { Generator.enum<EmptyEnum>() }
    }

    @Test
    fun generateAllGivenSamplesWithSameProbability() {
        val counts = IntArray(5)

        generator.randoms(-37).take(10_000).forEach {
            counts[it.ordinal]++
        }

        assertTrue(counts.all { it in 1800..2200 })
    }

    @Test
    fun provideNoSamples() {
        assertTrue(generator.samples.isEmpty())
    }
}
