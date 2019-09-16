package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IntGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.ints()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.ints(3, -2)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.ints(-8, 14).randomSequence(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(Int.MIN_VALUE, Int.MAX_VALUE, -1, 0, 1), Generator.ints().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(42, Int.MAX_VALUE), Generator.ints(min = 42).samples)
    }
}

class PositiveIntGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.positiveInts()

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.positiveInts(max = -1)
        }
    }

    @Test
    fun producePositiveIntegers() {
        assertTrue(Generator.positiveInts().randomSequence(0).take(1000).all { it >= 0 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.positiveInts(max = 42).randomSequence(0).take(1000).all { it >= 0 && it <= 42 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(0, 1, Int.MAX_VALUE), Generator.positiveInts().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(0, 1, 42), Generator.positiveInts(max = 42).samples)
    }
}

class NaturalIntGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.naturalInts()

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.naturalInts(max = 0)
        }
    }

    @Test
    fun produceNaturalIntegers() {
        assertTrue(Generator.naturalInts().randomSequence(0).take(1000).all { it >= 1 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.naturalInts(max = 42).randomSequence(0).take(1000).all { it >= 1 && it <= 42 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(1, Int.MAX_VALUE), Generator.naturalInts().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(1, 42), Generator.naturalInts(max = 42).samples)
    }
}

class NegativeIntGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.negativeInts()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.negativeInts(min = 0)
        }
    }

    @Test
    fun produceNegativeIntegers() {
        assertTrue(Generator.negativeInts().randomSequence(0).take(1000).all { it < 0 })
    }

    @Test
    fun produceBiggerThanMin() {
        assertTrue(Generator.negativeInts(min = -42).randomSequence(0).take(1000).all { it < 0 && it >= -42 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(-1, Int.MIN_VALUE), Generator.negativeInts().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(-1, -42), Generator.negativeInts(min = -42).samples)
    }
}

class NonZeroIntsTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.nonZeroInts()

    @Test
    fun failForInvalidMin() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroInts(min = 0)
        }
    }

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.nonZeroInts(min = 0)
        }
    }

    @Test
    fun produceNonZeroLongs() {
        assertTrue(Generator.nonZeroInts().randomSequence(0).take(1000).all { it != 0 })
    }

    @Test
    fun produceInRange() {
        val gen = Generator.nonZeroInts(min = -42, max = 100)
        assertTrue(gen.randomSequence(0).take(1000).all { it >= -42 && it <= 100 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(-1, 1, Int.MIN_VALUE, Int.MAX_VALUE), Generator.nonZeroInts().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(-1, 1, -42, 42), Generator.nonZeroInts(min = -42, max = 42).samples)
    }
}
