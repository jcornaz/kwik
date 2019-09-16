package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateGeneratorTest {

    @Test
    fun isPredictable() {
        val generator = Generator.create { it.nextInt() }

        val generation1 = generator.randomSequence(42).take(200).toList()
        val generation2 = generator.randomSequence(42).take(200).toList()

        assertEquals(generation1, generation2)
    }

    @Test
    fun useValuesReturnedInLambda() {
        val sequence = generateSequence(0) { it + 1 }

        val iterator = sequence.iterator()
        val generation = Generator.create { iterator.next() }.randomSequence(0).take(200).toList()

        assertEquals(sequence.take(200).toList(), generation)
    }

    @Test
    fun hasNoSample() {
        assertTrue(Generator.create { it.nextDouble() }.samples.isEmpty())
    }
}
