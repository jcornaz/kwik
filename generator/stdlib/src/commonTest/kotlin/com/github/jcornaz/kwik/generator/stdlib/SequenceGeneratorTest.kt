package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SequenceGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Sequence<Int>> = Generator.sequences()

    @Test
    fun generateOfAllSizeInGivenRange() {
        val sizes = mutableSetOf<Int>()

        repeat(100) {
            val size = Generator.sequences<Int>(minSize = 3, maxSize = 12).generate(Random).count()
            assertTrue(size in 3..12)
            sizes += size
        }

        assertEquals(10, sizes.size)
    }

    @Test
    fun sequenceArePredictable() {
        repeat(100) {
            assertEquals(
                Generator.sequences<Int>().generate(Random(it.toLong())).toList(),
                Generator.sequences<Int>().generate(Random(it.toLong())).toList()
            )
        }
    }
}
