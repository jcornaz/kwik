package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class MapGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Map<Int, Double>> = Generator.maps()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.maps(Generator.ints(), Generator.doubles(), minSize = 3, maxSize = 12)
            .randomSequence(0)
            .take(200)

        assertTrue(values.all { it.size in 3..12 })
    }

    @Test
    fun samplesContainsEmpty() {
        assertTrue(Generator.maps<Int, Double>().samples.any { it.isEmpty() })
    }

    @Test
    fun samplesContainsSingletons() {
        assertTrue(Generator.maps<Int, Double>().samples.any { it.size == 1 })
    }

    @Test
    fun noEmptySampleWhenMinSizeIsGreaterThan0() {
        assertTrue(Generator.maps(Generator.ints(), Generator.doubles(), minSize = 1).samples.none { it.isEmpty() })
    }

    @Test
    fun noSingletonSampleWhenMinSizeIsGreaterThan1() {
        assertTrue(Generator.maps(Generator.ints(), Generator.doubles(), minSize = 2).samples.none { it.size <= 1 })
    }

    @Test
    fun bigMinSizeIsPossible() {
        val generator = Generator.maps(Generator.ints(), Generator.ints(), minSize = 1000)

        assertEquals(1000, generator.randomSequence(1).first().size)
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.maps(Generator.ints(), Generator.doubles())
            .randomSequence(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 10)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Map<Int, Double>>()

        Generator.maps<Int, Double>().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }


    @Test
    fun failsWhenMinSizeIsNotPossible() {
        val keyGenerator = Generator.create { it.nextInt(0, 3) }

        assertFails {
            Generator.maps(keyGenerator, Generator.ints(), minSize = 4).randomSequence(0).first()
        }
    }
}

class NonEmptyMapGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Map<Int, Double>> = Generator.nonEmptyMaps()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.nonEmptyMaps(Generator.ints(), Generator.doubles(), maxSize = 12)
            .randomSequence(0)
            .take(200)

        assertTrue(values.all { it.size in 1..12 })
    }

    @Test
    fun samplesDoesNotContainsEmpty() {
        assertTrue(Generator.nonEmptyMaps<Int, Double>().samples.none { it.isEmpty() })
    }

    @Test
    fun samplesContainsSingletons() {
        assertTrue(Generator.nonEmptyMaps<Int, Double>().samples.any { it.size == 1 })
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.nonEmptyMaps(Generator.ints(), Generator.doubles())
            .randomSequence(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 10)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Map<Int, Double>>()

        Generator.nonEmptyMaps<Int, Double>().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
