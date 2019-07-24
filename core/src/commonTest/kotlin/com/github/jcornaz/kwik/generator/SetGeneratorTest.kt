package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class SetGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Set<Int>> = Generator.sets()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.sets(Generator.ints(), minSize = 3, maxSize = 12)
            .randoms(0)
            .take(200)

        assertTrue(values.all { it.size in 3..12 })
    }

    @Test
    fun samplesContainsEmpty() {
        assertTrue(Generator.sets<Int>().samples.any { it.isEmpty() })
    }

    @Test
    fun samplesContainsSingletons() {
        assertTrue(Generator.sets<Int>().samples.any { it.size == 1 })
    }

    @Test
    fun noEmptySampleWhenMinSizeIsGreaterThan0() {
        assertTrue(Generator.sets(Generator.ints(), minSize = 1).samples.none { it.isEmpty() })
    }

    @Test
    fun noSingletonSampleWhenMinSizeIsGreaterThan1() {
        assertTrue(Generator.sets(Generator.ints(), minSize = 2).samples.none { it.size <= 1 })
    }

    @Test
    fun bigMinSizeIsPossible() {
        assertEquals(1000, Generator.sets(Generator.ints(), minSize = 1000).randoms(1).first().size)
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.sets(Generator.ints())
            .randoms(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 60)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Set<Int>>()

        Generator.sets<Int>().randoms(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }

    @Test
    fun failsWhenMinSizeIsNotPossible() {
        val elementGenerator = Generator.create { it.nextInt(0, 3) }

        assertFails {
            Generator.sets(elementGenerator, minSize = 4).randoms(0).first()
        }
    }
}

class NonEmptySetGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Set<Int>> = Generator.nonEmptySets()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.nonEmptySets(Generator.ints(), maxSize = 12)
            .randoms(0)
            .take(200)

        assertTrue(values.all { it.size in 1..12 })
    }

    @Test
    fun samplesDoesNotContainsEmpty() {
        assertTrue(Generator.nonEmptySets<Int>().samples.none { it.isEmpty() })
    }

    @Test
    fun samplesContainsSingletons() {
        assertTrue(Generator.nonEmptySets<Int>().samples.any { it.size == 1 })
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.nonEmptySets(Generator.ints())
            .randoms(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 60)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Set<Int>>()

        Generator.nonEmptySets<Int>().randoms(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
