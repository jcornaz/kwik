package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<List<Int>> = Generator.lists()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.lists(Generator.ints(), minSize = 3, maxSize = 12)
            .randoms(0)
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
        assertEquals(1000, Generator.lists(Generator.ints(), minSize = 1000).randoms(1).first().size)
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.lists(Generator.ints())
            .randoms(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 60)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<List<Int>>()

        Generator.lists<Int>().randoms(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
