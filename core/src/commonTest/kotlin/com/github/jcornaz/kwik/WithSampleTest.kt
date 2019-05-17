package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertEquals

class WithSampleTest {

    @Test
    fun startsWithSamples() {
        val firstValues = Generator.create { it.nextInt(5, Int.MAX_VALUE) }
            .withSamples(1, 2, 3, 4)
            .randoms(42)
            .take(4)
            .toList()

        assertEquals(listOf(1, 2, 3, 4), firstValues)
    }

    @Test
    fun respectsGivenRatio() {
        val sampleOccurrenceCount = Generator.create { it.nextInt(5, Int.MAX_VALUE) }
            .withSamples(1, 2, 3, ratio = 0.4)
            .randoms(42)
            .take(100)
            .count { it in 1..3 }

        assertEquals(40, sampleOccurrenceCount)
    }

    @Test
    fun isPredictable() {
        val generator = Generator.create { it.nextInt() }.withSamples(1, 2, 3, 4)

        val generation1 = generator.randoms(5).take(200).toList()
        val generation2 = generator.randoms(5).take(200).toList()

        assertEquals(generation1, generation2)
    }
}
