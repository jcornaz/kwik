package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FloatGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.floats()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.floats(3f, -2f)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.floats(-8f, 14f).randoms(42).take(200).all { it >= -8 && it <= 14 })
    }

    @Test
    fun startsSamples() {
        val samples = Generator.floats().randoms(42).take(3).toSet()

        assertEquals(setOf(0f, -1f, 1f), samples)
    }
}
