@file:Suppress("EmptyRange")

package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertTrue

class RangeGeneratorTest : AbstractGeneratorTest() {
    override val generator = Generator.ranges(Generator.booleans())

    @Test
    fun `generates all possible ranges`() {
        // False .. True
        // False .. False
        // True .. True
        // Empty

        assertTrue(
            generator.randomSequence(0L)
                .take(100)
                .distinct()
                .count() == 4
        )
    }
}

class IntRangeGeneratorTest : AbstractGeneratorTest() {

    override val generator = Generator.intRanges()

    @Test
    fun `empty range is sampled`() {
        generator.randomSequence(0L)
            .take(10)
            .contains(1..0)
    }

    @Test
    fun `single element range is sampled`() {
        generator.randomSequence(0L)
            .take(10)
            .contains(1..1)
    }
}

class CharRangeGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<ClosedRange<Char>> = Generator.charRanges()

    @Test
    fun `empty range is sampled`() {
        generator.randomSequence(0L)
            .take(10)
            .contains('C'..'B')
    }

    @Test
    fun `single element range is sampled`() {
        generator.randomSequence(0L)
            .take(10)
            .contains('A'..'A')
    }
}


class LongRangeGeneratorTest : AbstractGeneratorTest() {
    override val generator = Generator.longRanges()

    @Test
    fun `empty range is sampled`() {
        generator.randomSequence(0L)
            .take(10)
            .contains(1L..0L)
    }

    @Test
    fun `single element range is sampled`() {
        generator.randomSequence(0L)
            .take(10)
            .contains(1L..1L)
    }
}
