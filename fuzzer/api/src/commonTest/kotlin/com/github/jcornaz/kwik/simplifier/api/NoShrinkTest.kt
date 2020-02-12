package com.github.jcornaz.kwik.simplifier.api

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class NoShrinkTest {

    @Test
    fun noShrinkerAlwaysReturnAnEmptySequence() {
        val shrinker = dontSimplify<Int>()
        repeat(100) {
            assertTrue(shrinker.simplify(Random.nextInt()).none())
        }
    }
}