package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.random.Random
import kotlin.test.*

class SetGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Set<Int>> = Generator.sets()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.sets(Generator.ints(), minSize = 3, maxSize = 12)
            .randomSequence(0)
            .take(200)

        assertTrue(values.all { it.size in 3..12 })
    }

    @Test
    fun failWithInvalidSize() {
        assertFailsWith<IllegalArgumentException> {
            Generator.sets<Int>(minSize = -2)
        }
    }

    @Test
    fun generatesEmpty() {
        assertTrue(Generator.sets<Int>().randomSequence(0).take(50).any { it.isEmpty() })
    }

    @Test
    fun generatesSingletons() {
        assertTrue(Generator.sets<Int>().randomSequence(0).take(50).any { it.size == 1 })
    }

    @Test
    fun doesNotGenerateEmptyWhenMinSizeIsGreaterThan0() {
        val gen = Generator.sets(Generator.ints(), minSize = 1)
        assertTrue(gen.randomSequence(0).take(1000).none { it.isEmpty() })
    }

    @Test
    fun doesNotGenerateSingletonWhenMinSizeIsGreaterThan1() {
        val gen = Generator.sets(Generator.ints(), minSize = 2)
        assertTrue(gen.randomSequence(0).take(1000).none { it.size == 1 })
    }

    @Test
    fun bigMinSizeIsPossible() {
        assertEquals(1000, Generator.sets(Generator.ints(), minSize = 1000).randomSequence(1).first().size)
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.sets(Generator.ints())
            .randomSequence(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 10)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Set<Int>>()

        Generator.sets<Int>().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }

    @Test
    fun failsWhenMinSizeIsNotPossible() {
        val elementGenerator = Generator { it: Random -> it.nextInt(0, 3) }

        assertFails {
            Generator.sets(elementGenerator, minSize = 4).randomSequence(0).first()
        }
    }
}

class NonEmptySetGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Set<Int>> = Generator.nonEmptySets()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.nonEmptySets(Generator.ints(), maxSize = 12)
            .randomSequence(0)
            .take(200)

        assertTrue(values.all { it.size in 1..12 })
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.nonEmptySets(Generator.ints())
            .randomSequence(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 10)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Set<Int>>()

        Generator.nonEmptySets<Int>().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
