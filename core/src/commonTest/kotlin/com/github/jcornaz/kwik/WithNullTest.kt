package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WithNullTest {

    @Test
    fun startsWithNull() {
        assertNull(Generator.create { Any() }.withNull().randoms(42).first())
    }

    @Test
    fun respectGivenRatio() {
        val values: Sequence<Any?> = Generator.create { Any() }.withNull(ratio = 0.4).randoms(12).take(100)

        assertEquals(40, values.count { it == null })
    }

    @Test
    fun isPredictable() {
        val generator = Generator.create { it.nextInt() }.withNull()

        val generation1 = generator.randoms(5).take(200).toList()
        val generation2 = generator.randoms(5).take(200).toList()

        assertEquals(generation1, generation2)
    }
}
