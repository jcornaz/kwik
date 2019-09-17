package com.github.jcornaz.kwik.generator.test

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class AbstractGeneratorTest {
    abstract val generator: Generator<*>

    @Test
    open fun isPredictable() {
        repeat(100) {
            assertEquals(generator.generate(Random(it.toLong())), generator.generate(Random(it.toLong())))
        }
    }

    @Test
    open fun isRandom() {
        val generation1 = (0 until 100).map { generator.generate(Random) }
        val generation2 = (0 until 100).map { generator.generate(Random) }

        assertNotEquals(generation1, generation2)
    }
}
