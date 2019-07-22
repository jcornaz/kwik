package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LongGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.longs()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.longs(3, -2)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.longs(-8, 14).randoms(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(Long.MIN_VALUE, Long.MAX_VALUE, -1, 0, 1), Generator.longs().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(42, Long.MAX_VALUE), Generator.longs(min = 42).samples)
    }
}

class PositiveLongGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.positiveLongs()

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.positiveLongs(max = -1)
        }
    }

    @Test
    fun producePositiveLongs() {
        assertTrue(Generator.positiveLongs().randoms(0).take(1000).all { it >= 0 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.positiveLongs(max = 42).randoms(0).take(1000).all { it >= 0 && it <= 42 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(0, 1, Long.MAX_VALUE), Generator.positiveLongs().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(0L, 1L, 42L), Generator.positiveLongs(max = 42).samples)
    }
}

class NaturalLongGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.naturalLongs()

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.naturalLongs(max = 0)
        }
    }

    @Test
    fun produceNaturalLongs() {
        assertTrue(Generator.naturalLongs().randoms(0).take(1000).all { it >= 1 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.naturalLongs(max = 42).randoms(0).take(1000).all { it >= 1 && it <= 42 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(1, Long.MAX_VALUE), Generator.naturalLongs().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(1L, 42L), Generator.naturalLongs(max = 42).samples)
    }
}

class NegativeLongGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.negativeLongs()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.negativeLongs(min = 0)
        }
    }

    @Test
    fun produceNegativeLongs() {
        assertTrue(Generator.negativeLongs().randoms(0).take(1000).all { it < 0 })
    }

    @Test
    fun produceBiggerThanMin() {
        assertTrue(Generator.negativeLongs(min = -42).randoms(0).take(1000).all { it < 0 && it >= -42 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(-1, Long.MIN_VALUE), Generator.negativeLongs().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(-1L, -42L), Generator.negativeLongs(min = -42).samples)
    }
}

class NonZeroLongsTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.nonZeroLongs()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroLongs(min = 0)
        }
    }

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroLongs(min = 0)
        }
    }


    @Test
    fun produceNonZeroLongs() {
        assertTrue(Generator.nonZeroLongs().randoms(0).take(1000).all { it != 0L })
    }

    @Test
    fun produceInRange() {
        val gen = Generator.nonZeroLongs(min = -42, max = 100)
        assertTrue(gen.randoms(0).take(1000).all { it >= -42 && it <= 100 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(-1L, 1L, Long.MIN_VALUE, Long.MAX_VALUE), Generator.nonZeroLongs().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(-1L, 1L, -42L, 42L), Generator.nonZeroLongs(min = -42, max = 42).samples)
    }
}
