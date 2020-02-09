package com.github.jcornaz.kwik.fuzzer

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

class NoShrinkTest {

    @Test
    fun noShrinkerAlwaysReturnAnEmptySequence() {
        val shrinker = noShrink<Int>()
        repeat(100) {
            assertTrue(shrinker.shrink(Random.nextInt()).none())
        }
    }
}