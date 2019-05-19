package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
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
    fun emitsEmptyLists() {
        assertTrue(Generator.sets<Int>().randoms(0).take(200).any { it.isEmpty() })
    }

    @Test
    fun emitsSingletonsLists() {
        assertTrue(Generator.sets<Int>().randoms(0).take(200).any { it.size == 1 })
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
