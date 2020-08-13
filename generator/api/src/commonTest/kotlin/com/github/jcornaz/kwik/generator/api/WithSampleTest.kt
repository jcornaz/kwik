package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.random.Random
import kotlin.test.*

class WithSampleTest : AbstractGeneratorTest() {

    override val generator: Generator<Int> =
        Generator { it: Random -> it.nextInt(5, Int.MAX_VALUE) }
            .withSamples(1, 2, 3, 4)

    @Test
    fun emptyListOfSamplesReturnOriginalGenerator() {
        val gen = Generator { it: Random -> it.nextInt() }
        assertSame(gen, gen.withSamples())
    }

    @Test
    fun failsIfProbabilityIsBellowZero() {
        val source = Generator { it: Random -> it.nextInt(5, Int.MAX_VALUE) }
        assertFailsWith<IllegalArgumentException> {
            source.withSamples(1, 2, 3, 4, probability = -1.0)
        }
    }

    @Test
    fun failsIfProbabilityIsZero() {
        val source = Generator { it: Random -> it.nextInt(5, Int.MAX_VALUE) }
        assertFailsWith<IllegalArgumentException> {
            source.withSamples(1, 2, 3, 4, probability = 0.0)
        }
    }

    @Test
    fun failsIfProbabilityOne() {
        val source = Generator { it: Random -> it.nextInt(5, Int.MAX_VALUE) }
        assertFailsWith<IllegalArgumentException> {
            source.withSamples(1, 2, 3, 4, probability = 1.0)
        }
    }

    @Test
    fun failsIfProbabilityAboveOne() {
        val source = Generator { it: Random -> it.nextInt(5, Int.MAX_VALUE) }
        assertFailsWith<IllegalArgumentException> {
            source.withSamples(1, 2, 3, 4, probability = 1.01)
        }
    }

    @Test
    fun generateSamplesAtGivenProbability() {
        val sampleCount = Generator { it: Random -> it.nextInt(5, Int.MAX_VALUE) }
            .withSamples(1, 2, 3, 4, probability = 0.5)
            .randomSequence(0)
            .take(1000)
            .count { it < 5 }

        assertTrue(sampleCount in 420..580)
    }
}

class WithNullTest : AbstractGeneratorTest() {

    override val generator: Generator<Int?> =
        Generator { it: Random -> it.nextInt(5, Int.MAX_VALUE) }.withNull()

    @Test
    fun generatesNull() {
        assertTrue(Generator { it: Random -> Any() }.withNull().randomSequence(0).take(50).any { it == null })
    }
}
