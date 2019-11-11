package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertTrue

class AndThenTest : AbstractGeneratorTest() {
    override val generator: Generator<Int> = Generator.create { it.nextInt() }
        .andThen { x -> Generator.create { it.nextInt() * x } }

    @Test
    fun applyTransform() {
        val gen: Generator<Int> = Generator.create { it.nextInt(2, 9) }
            .andThen { a ->
                Generator.create { a + it.nextInt(10, 11) }
            }

        gen
            .randomSequence(0)
            .take(200)
            .forEach {
                assertTrue(it in 12..20)
            }
    }
}
