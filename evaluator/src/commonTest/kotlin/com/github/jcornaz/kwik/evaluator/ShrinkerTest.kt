package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ShrinkerTest {

    @Test
    fun returnsInitialValueIfGeneratorReturnsAnEmptySmallestListOfValue() {
        val generator = object : Generator<Int> {
            override val samples: Set<Int> get() = emptySet()

            override fun randoms(seed: Long): Sequence<Int> = randomSequence(seed) { it.nextInt() }

            override fun shrink(value: Int): List<Int> = emptyList()
        }

        assertEquals(
            expected = 42,
            actual = generator.shrink(42) { fail("property should not be evaluated") }
        )
    }
}
