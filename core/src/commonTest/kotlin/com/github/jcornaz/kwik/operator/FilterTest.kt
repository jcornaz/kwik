package com.github.jcornaz.kwik.operator

import com.github.jcornaz.kwik.*
import com.github.jcornaz.kwik.generator.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class FilterTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.create { it.nextInt() }.filter { true }

    @Test
    fun filterValues() {
        val gen: Generator<Int> = Generator.create { it.nextInt() }.filter { it % 2 == 0 }

        gen.randoms(0).take(1000).forEach {
            assertEquals(0, it % 2)
        }
    }

    @Test
    fun filterSamples() {
        val generator = Generator.create { it.nextInt() }
            .withSamples(42, 77)
            .filter { it < 50 }

        assertEquals(setOf(42), generator.samples)
    }
}

class FilterNotTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.create { it.nextInt() }.filterNot { false }

    @Test
    fun filterValues() {
        val gen: Generator<Int> = Generator.create { it.nextInt() }.filterNot { it % 2 == 0 }

        gen.randoms(0).take(1000).forEach {
            assertNotEquals(0, it % 2)
        }
    }

    @Test
    fun filterSamples() {
        val generator = Generator.create { it.nextInt() }
            .withSamples(42, 77)
            .filterNot { it < 50 }

        assertEquals(setOf(77), generator.samples)
    }
}
