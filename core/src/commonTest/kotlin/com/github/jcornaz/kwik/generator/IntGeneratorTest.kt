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
        assertTrue(Generator.ints(-8, 14).randoms(42).take(200).all { it >= -8 && it <= 14 })
    }

    @Test
    fun startsWithSamples() {
        val samples = Generator.ints().randoms(42).take(5).toSet()

        assertEquals(setOf(Int.MIN_VALUE, Int.MAX_VALUE, -1, 0, 1), samples)
    }
}
