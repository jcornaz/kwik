package com.github.jcornaz.kwik.generator

import kotlin.test.Test
import kotlin.test.assertEquals

class RandomSequenceTest {

    @Test
    fun isPredictable() {
        val sequence = randomSequence(123) { it.nextInt() }

        assertEquals(sequence.take(200).toList(), sequence.take(200).toList())
    }

    @Test
    fun isInfinite() {
        assertEquals(100_000, randomSequence(1) { it.nextInt() }.take(100_000).count())
    }
}
