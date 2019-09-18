package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<List<Int>> = Generator.lists()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.lists(Generator.ints(), minSize = 3, maxSize = 12)
            .randomSequence(0)
            .take(200)

        assertTrue(values.all { it.size in 3..12 })
    }

    @Test
    fun samplesContainsEmpty() {
        assertTrue(Generator.lists<Int>().samples.any { it.isEmpty() })
    }

    @Test
    fun samplesContainsSingletons() {
        assertTrue(Generator.lists<Int>().samples.any { it.size == 1 })
    }

    @Test
    fun noEmptySampleWhenMinSizeIsGreaterThan0() {
        assertTrue(Generator.lists(Generator.ints(), minSize = 1).samples.none { it.isEmpty() })
    }

    @Test
    fun noSingletonSampleWhenMinSizeIsGreaterThan1() {
        assertTrue(Generator.lists(Generator.ints(), minSize = 2).samples.none { it.size <= 1 })
    }

    @Test
    fun bigMinSizeIsPossible() {
        assertEquals(1000, Generator.lists(Generator.ints(), minSize = 1000).randomSequence(1).first().size)
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.lists(Generator.ints())
            .randomSequence(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 10)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<List<Int>>()

        Generator.lists<Int>().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}

class NonEmptyListGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<List<Int>> = Generator.nonEmptyLists()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.nonEmptyLists(Generator.ints(), maxSize = 12)
            .randomSequence(0)
            .take(200)

        assertTrue(values.all { it.size in 1..12 })
    }

    @Test
    fun samplesDoesNotContainsEmpty() {
        assertTrue(Generator.nonEmptyLists<Int>().samples.none { it.isEmpty() })
    }

    @Test
    fun samplesContainsSingletons() {
        assertTrue(Generator.nonEmptyLists<Int>().samples.any { it.size == 1 })
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.nonEmptyLists(Generator.ints(), maxSize = 200)
            .randomSequence(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 10)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<List<Int>>()

        Generator.nonEmptyLists<Int>().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
