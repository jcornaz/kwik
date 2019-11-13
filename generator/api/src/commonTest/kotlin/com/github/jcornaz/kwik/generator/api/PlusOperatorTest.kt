package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PlusOperatorTest : AbstractGeneratorTest() {
    override val generator: Generator<Int>
        get() = Generator.create { it.nextInt(-10, 0) } + Generator.create { it.nextInt(1, 11) }

    @Test
    fun generateFromBothGenerators() {
        val gen1 = Generator.create { it.nextInt(-100, 0) }
        val gen2 = Generator.create { it.nextInt(1, 101) }

        val values = (gen1 + gen2).randomSequence(0).take(1000)

        assertTrue(values.any { it > 0 })
        assertTrue(values.any { it < 0 })
    }
}
