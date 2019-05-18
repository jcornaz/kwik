package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WithSampleTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt(5, Int.MAX_VALUE) }
            .withSamples(1, 2, 3, 4)

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
}

class WithNullTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt(5, Int.MAX_VALUE) }
            .withNull()

    @Test
    fun startsWithNull() {
        assertNull(Generator.create { Any() }.withNull().randoms(42).first())
    }

    @Test
    fun respectGivenRatio() {
        val values: Sequence<Any?> = Generator.create { Any() }.withNull(ratio = 0.4).randoms(12).take(100)

        assertEquals(40, values.count { it == null })
    }
}
