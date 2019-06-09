package com.github.jcornaz.kwik.operator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.filter
import com.github.jcornaz.kwik.filterNot
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
}
