package com.github.jcornaz.kwik.simplifier.api

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class NoSimplifierTest {

    @Test
    fun alwaysReturnAnEmptySequence() {
        repeat(100) {
            assertTrue(dontSimplify<Int>().simplify(Random.nextInt()).none())
        }
    }
}