package com.github.jcornaz.kwik.generator.test

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class AbstractGeneratorTest {
    abstract val generator: Generator<*>

    @Test
    fun isPredictable() {
        val generation1 = generator.randomSequence(1).take(200).toList()
        val generation2 = generator.randomSequence(1).take(200).toList()

        assertEquals(generation1, generation2)
    }

    @Test
    fun isRandom() {
        val generation1 = generator.randomSequence(0).take(200).toList()
        val generation2 = generator.randomSequence(1).take(200).toList()

        assertNotEquals(generation1, generation2)
    }

    @Test
    fun isInfinite() {
        assertEquals(100_000, generator.randomSequence(12).take(100_000).count())
    }
}
