package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class NoSimplifierTest {

    @Test
    fun alwaysReturnAnEmptySequence() {
        repeat(100) {
            assertTrue(dontSimplify<Int>().tree(Random.nextInt()).children.none())
        }
    }
}