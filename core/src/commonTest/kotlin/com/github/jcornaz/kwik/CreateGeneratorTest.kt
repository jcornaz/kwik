package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateGeneratorTest {

    @Test
    fun doNotContainsAnyEdgeCase() {
        assertTrue(Generator.create { }.edgeCases.isEmpty())
    }

    @Test
    fun isPredictable() {
        val generator = Generator.create { it.nextInt() }

        val generation1 = generator.randoms(42).take(200).toList()
        val generation2 = generator.randoms(42).take(200).toList()

        assertEquals(generation1, generation2)
    }

    @Test
    fun useValuesReturnedInLambda() {
        val sequence = generateSequence(0) { it + 1 }

        val iterator = sequence.iterator()
        val generation = Generator.create { iterator.next() }.randoms(0).take(200).toList()

        assertEquals(sequence.take(200).toList(), generation)
    }
}
