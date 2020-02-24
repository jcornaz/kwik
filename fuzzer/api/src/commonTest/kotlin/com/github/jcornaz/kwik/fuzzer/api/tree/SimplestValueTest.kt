package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.roseTreeOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class SimplestValueTest {

    @Test
    fun hasNoBranch() {
        repeat(100) {
            assertTrue(roseTreeOf(Random.nextInt()).children.none())
        }
    }

    @Test
    fun hasValueAsRoot() {
        repeat(100) {
            val value = Random.nextInt()
            assertEquals(value, roseTreeOf(
                value
            ).item)
        }
    }
}