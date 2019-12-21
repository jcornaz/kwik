package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.*

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
    fun failWithInvalidSize() {
        assertFailsWith<IllegalArgumentException> {
            Generator.maps<Int, Double>(minSize = -2)
        }
    }

    @Test
    fun generatesEmpty() {
        assertTrue(Generator.maps<Int, Double>().randomSequence(0).take(50).any { it.isEmpty() })
    }

    @Test
    fun generateSingletons() {
        assertTrue(Generator.maps<Int, Double>().randomSequence(0).take(50).any { it.size == 1 })
    }

    @Test
    fun doesNotGenerateEmptyWhenMinSizeIsGreaterThan0() {
        val generator = Generator.maps(Generator.ints(), Generator.doubles(), minSize = 1)
        assertTrue(generator.randomSequence(0).take(1000).none { it.isEmpty() })
    }

    @Test
    fun doesNotGeneratSingletonWhenMinSizeIsGreaterThan1() {
        val generator = Generator.maps(Generator.ints(), Generator.doubles(), minSize = 2)
        assertTrue(generator.randomSequence(0).take(1000).none { it.size <= 1 })
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
