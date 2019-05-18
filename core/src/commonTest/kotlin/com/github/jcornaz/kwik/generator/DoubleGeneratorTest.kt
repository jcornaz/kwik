package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DoubleGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.doubles()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.doubles(3.0, -2.0)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        assertTrue(Generator.doubles(-8.0, 14.0).randoms(42).take(200).all { it >= -8 && it <= 14 })
    }

    @Test
    fun startsWithEdgeCases() {
        val edgeCases = Generator.doubles().randoms(42).take(3).toSet()

        assertEquals(setOf(0.0, -1.0, 1.0), edgeCases)
    }
}
