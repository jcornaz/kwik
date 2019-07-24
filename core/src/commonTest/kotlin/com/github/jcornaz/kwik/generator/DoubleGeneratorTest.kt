package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.generator.stdlib.*
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DoubleGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Double> = Generator.doubles()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.doubles(3.0, -2.0)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.doubles(-8.0, 14.0).randoms(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun providesSamples() {
        assertEquals(setOf(0.0, -1.0, 1.0, -Double.MAX_VALUE, Double.MAX_VALUE), Generator.doubles().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(1.0, Double.MAX_VALUE), Generator.doubles(min = 1.0).samples)
    }

    @Test
    fun withNaNIncludesNaNInSamples() {
        assertTrue(Generator.doubles().withNaN().samples.any { it.isNaN() })
    }
}

class PositiveDoubleGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.positiveDoubles()

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.positiveDoubles(max = -0.5)
        }
    }

    @Test
    fun producePositiveIntegers() {
        assertTrue(Generator.positiveDoubles().randoms(0).take(1000).all { it >= 0 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.positiveDoubles(max = 42.0).randoms(0).take(1000).all { it >= 0.0 && it <= 42.0 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(0.0, 1.0, Double.MAX_VALUE), Generator.positiveDoubles().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(0.0, 1.0, 42.0), Generator.positiveDoubles(max = 42.0).samples)
    }
}

class NegativeDoubleGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.negativeDoubles()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.negativeDoubles(min = 0.0)
        }
    }

    @Test
    fun produceNegativeIntegers() {
        assertTrue(Generator.negativeDoubles().randoms(0).take(1000).all { it < 0 })
    }

    @Test
    fun produceBiggerThanMin() {
        assertTrue(Generator.negativeDoubles(min = -42.0).randoms(0).take(1000).all { it < 0.0 && it >= -42.0 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(-Double.MIN_VALUE, -Double.MAX_VALUE, -1.0), Generator.negativeDoubles().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(-Double.MIN_VALUE, -42.0, -1.0), Generator.negativeDoubles(min = -42.0).samples)
    }
}

class NonZeroDoubleTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.nonZeroDoubles()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroDoubles(min = 0.0)
        }
    }

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroDoubles(min = 0.0)
        }
    }

    @Test
    fun produceNonZeroLongs() {
        assertTrue(Generator.nonZeroDoubles().randoms(0).take(1000).all { it != 0.0 })
    }

    @Test
    fun produceInRange() {
        val gen = Generator.nonZeroDoubles(min = -42.0, max = 100.0)
        assertTrue(gen.randoms(0).take(1000).all { it >= -42 && it <= 100 })
    }

    @Test
    fun provideSamples() {
        assertEquals(
            setOf(-Double.MIN_VALUE, -1.0, -Double.MAX_VALUE, Double.MIN_VALUE, 1.0, Double.MAX_VALUE),
            Generator.nonZeroDoubles().samples
        )
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(
            setOf(-42.0, -1.0, -Double.MIN_VALUE, Double.MIN_VALUE, 1.0, 42.0),
            Generator.nonZeroDoubles(min = -42.0, max = 42.0).samples
        )
    }
}
