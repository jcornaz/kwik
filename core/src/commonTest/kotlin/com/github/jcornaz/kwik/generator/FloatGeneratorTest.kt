package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FloatGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.floats()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.floats(3f, -2f)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.floats(-8f, 14f).randoms(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun providesSamples() {
        assertEquals(setOf(0f, -1f, 1f, -Float.MAX_VALUE, Float.MAX_VALUE), Generator.floats().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(1f, Float.MAX_VALUE), Generator.floats(min = 1f).samples)
    }
}

class PositiveFloatGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.positiveFloats()

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.positiveFloats(max = -0.5f)
        }
    }

    @Test
    fun producePositiveIntegers() {
        assertTrue(Generator.positiveFloats().randoms(0).take(1000).all { it >= 0 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.positiveFloats(max = 42f).randoms(0).take(1000).all { it >= 0.0 && it <= 42.0 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(0f, 1f, Float.MAX_VALUE), Generator.positiveFloats().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(0f, 1f, 42f), Generator.positiveFloats(max = 42f).samples)
    }
}

class NegativeFloatGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.negativeFloats()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.negativeFloats(min = 0f)
        }
    }

    @Test
    fun produceNegativeIntegers() {
        assertTrue(Generator.negativeFloats().randoms(0).take(1000).all { it < 0 })
    }

    @Test
    fun produceBiggerThanMin() {
        assertTrue(Generator.negativeFloats(min = -42f).randoms(0).take(1000).all { it < 0.0 && it >= -42.0 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(-Float.MIN_VALUE, -Float.MAX_VALUE, -1f), Generator.negativeFloats().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(-Float.MIN_VALUE, -42f, -1f), Generator.negativeFloats(min = -42f).samples)
    }
}

class NonZeroFloatTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.nonZeroFloats()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroFloats(min = 0f)
        }
    }

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroFloats(min = 0f)
        }
    }

    @Test
    fun produceNonZeroLongs() {
        assertTrue(Generator.nonZeroFloats().randoms(0).take(1000).all { it != 0f })
    }

    @Test
    fun produceInRange() {
        val gen = Generator.nonZeroFloats(min = -42f, max = 100f)
        assertTrue(gen.randoms(0).take(1000).all { it >= -42 && it <= 100 })
    }

    @Test
    fun provideSamples() {
        assertEquals(
            setOf(-Float.MIN_VALUE, -1f, -Float.MAX_VALUE, Float.MIN_VALUE, 1f, Float.MAX_VALUE),
            Generator.nonZeroFloats().samples
        )
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(
            setOf(-42f, -1f, -Float.MIN_VALUE, Float.MIN_VALUE, 1f, 42f),
            Generator.nonZeroFloats(min = -42f, max = 42f).samples
        )
    }
}
