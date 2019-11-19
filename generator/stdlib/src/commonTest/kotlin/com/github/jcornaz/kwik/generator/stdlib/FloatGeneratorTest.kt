package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
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
        assertTrue(Generator.floats(-8f, 14f).randomSequence(0).take(1000).all { it >= -8 && it <= 14 })
    }


    @Test
    fun doNotProduceZeroIfNotInRange() {
        assertTrue(Generator.floats(1f, 14f).randomSequence(0).take(1000).none { it < 1f })
    }

    @Test
    fun generateZero() {
        assertTrue(Generator.floats().randomSequence(0).take(50).any { it == 0f })
    }

    @Test
    fun generateOne() {
        assertTrue(Generator.floats().randomSequence(0).take(50).any { it == 1f })
    }

    @Test
    fun generateMinusOne() {
        assertTrue(Generator.floats().randomSequence(0).take(50).any { it == -1f })
    }

    @Test
    fun generateMin() {
        assertTrue(Generator.floats(min = 42f).randomSequence(5).take(50).any { it == 42f })
    }

    @Test
    fun generateMax() {
        assertTrue(Generator.floats(max = 24f).randomSequence(0).take(50).any { it == 24f })
    }

    @Test
    fun generateBetweenZeroAndOne() {
        assertTrue(Generator.floats().randomSequence(0).take(50).any { it > 0.0 && it < 1.0 })
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
        assertTrue(Generator.positiveFloats().randomSequence(0).take(1000).all { it >= 0 })
    }

    @Test
    fun produceSmallerThanMax() {
        assertTrue(Generator.positiveFloats(max = 42f).randomSequence(0).take(1000).all { it in 0.0..42.0 })
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
        assertTrue(Generator.negativeFloats().randomSequence(0).take(1000).all { it < 0 })
    }

    @Test
    fun produceBiggerThanMin() {
        assertTrue(Generator.negativeFloats(min = -42f).randomSequence(0).take(1000).all { it < 0.0 && it >= -42.0 })
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
        assertTrue(Generator.nonZeroFloats().randomSequence(0).take(1000).all { it != 0f })
    }

    @Test
    fun produceInRange() {
        val gen = Generator.nonZeroFloats(min = -42f, max = 100f)
        assertTrue(gen.randomSequence(0).take(1000).all { it >= -42 && it <= 100 })
    }
}
