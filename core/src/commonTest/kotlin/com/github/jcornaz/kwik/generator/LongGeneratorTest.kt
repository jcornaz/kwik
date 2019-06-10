package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LongGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.longs()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.longs(3, -2)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.longs(-8, 14).randoms(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(Long.MIN_VALUE, Long.MAX_VALUE, -1, 0, 1), Generator.longs().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(42, Long.MAX_VALUE), Generator.longs(min = 42).samples)
    }
}
