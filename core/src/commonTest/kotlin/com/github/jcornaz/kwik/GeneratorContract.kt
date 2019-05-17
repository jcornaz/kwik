package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertEquals

abstract class GeneratorContract {
    abstract val generator: Generator<*>

    @Test
    fun isPredictable() {
        val generation1 = generator.randoms(42).take(200).toList()
        val generation2 = generator.randoms(42).take(200).toList()

        assertEquals(generation1, generation2)
    }

    @Test
    fun isInfinite() {
        assertEquals(100_000, generator.randoms(12).take(100_000).count())
    }
}
