package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

class DistinctTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator { it: Random -> it.nextInt() }.map { it.toString() }

    companion object {
        const val COLLISION_TAKEN_VALUES = 2000
    }

    @Test
    fun applyDistinction() {
        val gen: Generator<Int> = Generator { it.nextInt().absoluteValue / 2000 }

        // sequence known to produce collisions
        val result = gen.distinct().randomSequence(1).take(COLLISION_TAKEN_VALUES)
        assertTrue { COLLISION_TAKEN_VALUES == result.toList().size }

    }
}