package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
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
    fun emitsEmptyLists() {
        assertTrue(Generator.lists<Int>().randoms(0).take(200).any { it.isEmpty() })
    }

    @Test
    fun emitsSingletonsLists() {
        assertTrue(Generator.lists<Int>().randoms(0).take(200).any { it.size == 1 })
    }

    @Test
    fun generateOfManySize() {
        val sizes = mutableSetOf<Int>()

        Generator.lists(Generator.ints(), maxSize = 1000)
            .randoms(0)
            .take(1000)
            .forEach {
                sizes += it.size
            }

        assertTrue(sizes.size > 100)
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
