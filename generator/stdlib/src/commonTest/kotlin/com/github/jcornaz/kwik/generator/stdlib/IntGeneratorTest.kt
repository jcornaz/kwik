package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
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
    fun generateZero() {
        assertTrue(Generator.ints().randomSequence(0).take(50).any { it == 0 })
    }

    @Test
    fun generateOne() {
        assertTrue(Generator.ints().randomSequence(0).take(50).any { it == 1 })
    }

    @Test
    fun generateMinusOne() {
        assertTrue(Generator.ints().randomSequence(0).take(50).any { it == -1 })
    }

    @Test
    fun generateMin() {
        assertTrue(Generator.ints(min = 42).randomSequence(0).take(50).any { it == 42 })
    }

    @Test
    fun generateMax() {
        assertTrue(Generator.ints(max = 24).randomSequence(0).take(50).any { it == 24 })
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
        assertTrue(Generator.positiveInts(max = 42).randomSequence(0).take(1000).all { it in 0..42 })
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
        assertTrue(Generator.naturalInts(max = 42).randomSequence(0).take(1000).all { it in 1..42 })
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
}
