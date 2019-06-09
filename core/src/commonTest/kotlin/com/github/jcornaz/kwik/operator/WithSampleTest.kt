package com.github.jcornaz.kwik.operator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.withNull
import com.github.jcornaz.kwik.withSamples
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class WithSampleTest : AbstractGeneratorTest() {

    override val generator: Generator<Int> =
        Generator.create { it.nextInt(5, Int.MAX_VALUE) }
            .withSamples(1, 2, 3, 4)

    @Test
    fun hasSamples() {
        val generator = Generator.create { it.nextInt(5, Int.MAX_VALUE) }
            .withSamples(1, 2, 3, 4)

        assertEquals(setOf(1, 2, 3, 4), generator.samples)
    }

    @Test
    fun emptyListOfSamplesReturnOriginalGenerator() {
        val gen = Generator.create { it.nextInt() }
        assertSame(gen, gen.withSamples())
    }
}

class WithNullTest : AbstractGeneratorTest() {

    override val generator: Generator<Int?> =
        Generator.create { it.nextInt(5, Int.MAX_VALUE) }.withNull()

    @Test
    fun samplesContainsNull() {
        assertTrue(Generator.create { Any() }.withNull().samples.any { it == null })
    }
}
