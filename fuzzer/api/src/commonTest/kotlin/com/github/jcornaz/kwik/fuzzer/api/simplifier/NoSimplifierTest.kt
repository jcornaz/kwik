package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.ExperimentalKwikApi
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

@ExperimentalKwikApi
class NoSimplifierTest {

    @Test
    fun alwaysReturnAnEmptySequence() {
        repeat(100) {
            assertTrue(dontSimplify<Int>().simplify(Random.nextInt()).none())
        }
    }
}