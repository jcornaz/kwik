package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IntGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.ints()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.ints(3, -2)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.ints(-8, 14).randoms(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun provideSamples() {
        assertEquals(setOf(Int.MIN_VALUE, Int.MAX_VALUE, -1, 0, 1), Generator.ints().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(42, Int.MAX_VALUE), Generator.ints(min = 42).samples)
    }
}
