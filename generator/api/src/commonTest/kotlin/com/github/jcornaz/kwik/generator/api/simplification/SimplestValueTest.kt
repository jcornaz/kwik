package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalKwikGeneratorApi
class SimplestValueTest {

    @Test
    fun hasNoBranch() {
        repeat(100) {
            assertTrue(simplestValue(Random.nextInt()).children.none())
        }
    }

    @Test
    fun hasValueAsRoot() {
        repeat(100) {
            val value = Random.nextInt()
            assertEquals(value, simplestValue(value).root)
        }
    }
}