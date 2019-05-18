package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertTrue

class MapGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Map<Int, Double>> = Generator.maps()

    @Test
    fun generateInGivenSizeRange() {
        val values = Generator.maps(Generator.ints(), Generator.doubles(), minSize = 3, maxSize = 12)
            .randoms(0)
            .take(200)

        assertTrue(values.all { it.size in 3..12 })
    }

    @Test
    fun emitsEmptyLists() {
        assertTrue(Generator.maps<Int, Double>().randoms(0).take(200).any { it.isEmpty() })
    }

    @Test
    fun emitsSingletonsLists() {
        assertTrue(Generator.maps<Int, Double>().randoms(0).take(200).any { it.size == 1 })
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.maps(Generator.ints(), Generator.doubles(), maxSize = 1000)
            .randoms(0)
            .take(200)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 100)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Map<Int, Double>>()

        Generator.maps<Int, Double>().randoms(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
