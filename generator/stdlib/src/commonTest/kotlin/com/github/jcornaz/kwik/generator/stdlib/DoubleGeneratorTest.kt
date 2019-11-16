package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
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
        assertTrue(Generator.doubles(-8.0, 14.0).randomSequence(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun doNotProduceZeroIfNotInRange() {
        assertTrue(Generator.doubles(1.0, 14.0).randomSequence(0).take(1000).none { it < 1.0 })
    }

    @Test
    fun withNaNIncludesNaNInSamples() {
        assertTrue(Generator.doubles().withNaN().randomSequence(0).take(30).any { it.isNaN() })
    }

    @Test
    fun generateZero() {
        assertTrue(Generator.doubles().randomSequence(0).take(30).any { it == 0.0 })
    }

    @Test
    fun generateOne() {
        assertTrue(Generator.doubles().randomSequence(0).take(30).any { it == 1.0 })
    }

    @Test
    fun generateMinusOne() {
        assertTrue(Generator.doubles().randomSequence(0).take(30).any { it == -1.0 })
    }

    @Test
    fun generateMin() {
        assertTrue(Generator.doubles(min = 42.0).randomSequence(0).take(30).any { it == 42.0 })
    }

    @Test
    fun generateMax() {
        assertTrue(Generator.doubles(max = 24.0).randomSequence(0).take(30).any { it == 24.0 })
    }

    @Test
    fun generateBetweenZeroAndOne() {
        assertTrue(Generator.doubles().randomSequence(0).take(30).any { it > 0.0 && it < 1.0 })
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
        assertTrue(Generator.positiveDoubles().randomSequence(0).take(1000).all { it >= 0 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.positiveDoubles(max = 42.0).randomSequence(0).take(1000).all { it in 0.0..42.0 })
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
        assertTrue(Generator.negativeDoubles().randomSequence(0).take(1000).all { it < 0 })
    }

    @Test
    fun produceBiggerThanMin() {
        assertTrue(Generator.negativeDoubles(min = -42.0).randomSequence(0).take(1000).all { it < 0.0 && it >= -42.0 })
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
        assertTrue(Generator.nonZeroDoubles().randomSequence(0).take(1000).all { it != 0.0 })
    }

    @Test
    fun produceInRange() {
        val gen = Generator.nonZeroDoubles(min = -42.0, max = 100.0)
        assertTrue(gen.randomSequence(0).take(1000).all { it >= -42 && it <= 100 })
    }
}
