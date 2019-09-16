package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlusOperatorTest : AbstractGeneratorTest() {
    override val generator: Generator<Int>
        get() = Generator.create { it.nextInt(-10, 0) } + Generator.create { it.nextInt(1, 11) }

    @Test
    fun mergeSamples() {
        val gen1 = Generator.create { it.nextInt() }
            .withSamples(1, 2, 3)

        val gen2 = Generator.create { it.nextInt() }
            .withSamples(3, 4, 5)

        assertEquals(setOf(1, 2, 3, 4, 5), (gen1 + gen2).samples)
    }

    @Test
    fun generateFromBothGenerators() {
        val gen1 = Generator.create { it.nextInt(-100, 0) }
        val gen2 = Generator.create { it.nextInt(1, 101) }

        val values = (gen1 + gen2).randoms(0).take(1000)

        assertTrue(values.any { it > 0 })
        assertTrue(values.any { it < 0 })
    }
}
