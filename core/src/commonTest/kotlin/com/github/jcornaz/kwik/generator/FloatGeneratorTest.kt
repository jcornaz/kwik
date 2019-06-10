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
        assertTrue(Generator.floats(-8f, 14f).randoms(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun providesSamples() {
        assertEquals(setOf(0f, -1f, 1f, -Float.MAX_VALUE, Float.MAX_VALUE), Generator.floats().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(1f, Float.MAX_VALUE), Generator.floats(min = 1f).samples)
    }
}
