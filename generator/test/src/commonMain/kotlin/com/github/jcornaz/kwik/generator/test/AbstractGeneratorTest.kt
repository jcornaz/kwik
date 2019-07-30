package com.github.jcornaz.kwik.generator.test

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class AbstractGeneratorTest {
    abstract val generator: Generator<*>

    @Test
    fun isPredictable() {
        val generation1 = generator.randoms(1).take(200).toList()
        val generation2 = generator.randoms(1).take(200).toList()

        assertEquals(generation1, generation2)
    }

    @Test
    fun isRandom() {
        val generation1 = generator.randoms(0).take(200).toList()
        val generation2 = generator.randoms(1).take(200).toList()

        assertNotEquals(generation1, generation2)
    }

    @Test
    fun isInfinite() {
        assertEquals(100_000, generator.randoms(12).take(100_000).count())
    }
}

infix fun <T> Generator<T>.shouldBeEquivalentTo(other: Generator<T>) {
    assertEquals(other.samples, samples)

    repeat(5) {
        assertEquals(other.randoms(it.toLong()).take(100).toList(), randoms(it.toLong()).take(100).toList())
    }

    other.randoms(0L).take(5).forEach {
        assertEquals(other.shrink(it), shrink(it))
    }
}
