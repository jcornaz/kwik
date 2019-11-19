package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
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
        assertTrue(Generator.longs(-8, 14).randomSequence(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun generateZero() {
        assertTrue(Generator.longs().randomSequence(0).take(30).any { it == 0L })
    }

    @Test
    fun generateOne() {
        assertTrue(Generator.longs().randomSequence(0).take(30).any { it == 1L })
    }

    @Test
    fun generateMinusOne() {
        assertTrue(Generator.longs().randomSequence(0).take(30).any { it == -1L })
    }

    @Test
    fun generateMin() {
        assertTrue(Generator.longs(min = 42).randomSequence(0).take(30).any { it == 42L })
    }

    @Test
    fun generateMax() {
        assertTrue(Generator.longs(max = 24).randomSequence(0).take(30).any { it == 24L })
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
        assertTrue(Generator.positiveLongs().randomSequence(0).take(1000).all { it >= 0 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.positiveLongs(max = 42).randomSequence(0).take(1000).all { it in 0..42 })
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
        assertTrue(Generator.naturalLongs().randomSequence(0).take(1000).all { it >= 1 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.naturalLongs(max = 42).randomSequence(0).take(1000).all { it in 1..42 })
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
        assertTrue(Generator.negativeLongs().randomSequence(0).take(1000).all { it < 0 })
    }

    @Test
    fun produceBiggerThanMin() {
        assertTrue(Generator.negativeLongs(min = -42).randomSequence(0).take(1000).all { it < 0 && it >= -42 })
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
        assertTrue(Generator.nonZeroLongs().randomSequence(0).take(1000).all { it != 0L })
    }

    @Test
    fun produceInRange() {
        val gen = Generator.nonZeroLongs(min = -42, max = 100)
        assertTrue(gen.randomSequence(0).take(1000).all { it >= -42 && it <= 100 })
    }
}
