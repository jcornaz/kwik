package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DoubleGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Double> = Generator.doubles()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.doubles(3.0, -2.0)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.doubles(-8.0, 14.0).randoms(0).take(1000).all { it >= -8 && it <= 14 })
    }

    @Test
    fun providesSamples() {
        assertEquals(setOf(0.0, -1.0, 1.0, -Double.MAX_VALUE, Double.MAX_VALUE), Generator.doubles().samples)
    }

    @Test
    fun samplesAreInRange() {
        assertEquals(setOf(1.0, Double.MAX_VALUE), Generator.doubles(min = 1.0).samples)
    }
}
