package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
import kotlin.test.assertTrue

class BooleanGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.booleans()

    @Test
    fun produceTrueAndFalse() {
        val values: Sequence<Boolean> = Generator.booleans().randoms(42).take(200)
        assertTrue(values.any { it })
        assertTrue(values.any { !it })
    }
}
