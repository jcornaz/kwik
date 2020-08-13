package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

class AndThenTest : AbstractGeneratorTest() {
    override val generator: Generator<Int> = Generator { it: Random -> it.nextInt() }
        .andThen { x -> Generator { it: Random -> it.nextInt() * x } }

    @Test
    fun applyTransform() {
        val gen: Generator<Int> = Generator { it: Random -> it.nextInt(2, 9) }
            .andThen { a ->
                Generator { it: Random -> a + it.nextInt(10, 11) }
            }

        gen
            .randomSequence(0)
            .take(200)
            .forEach {
                assertTrue(it in 12..20)
            }
    }
}
