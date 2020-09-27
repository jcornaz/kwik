package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StringGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator.strings()

    @Test
    fun generateStringsOfGivenLengthRange() {
        val values = Generator.strings(minLength = 3, maxLength = 12).randomSequence(0).take(200)

        assertTrue(values.all { it.length in 3..12 })
    }

    @Test
    fun generateEmpty() {
        assertTrue(Generator.strings().randomSequence(0).take(50).any { it.isEmpty() })
    }

    @Test
    fun generateBlank() {
        assertTrue(Generator.strings().randomSequence(0).take(50).any { it.isNotEmpty() && it.isBlank() })
    }

    @Test
    fun doesNotGenerateEmptyWhenMinLengthIsGreaterThan0() {
        assertTrue(Generator.strings(minLength = 1).randomSequence(0).take(1000).none { it.isEmpty() })
    }

    @Test
    fun generateOfSizeBiggerThanMinMinLength() {
        assertTrue(Generator.strings(minLength = 2).randomSequence(0).take(1000).none { it.length <= 1 })
    }

    @Test
    fun bigMinLengthIsPossible() {
        val generator = Generator.strings(minLength = 1000)

        assertEquals(1000, generator.randomSequence(1).first().length)
    }

    @Test
    fun generateOfManyLength() {
        val lengths = mutableSetOf<Int>()

        Generator.strings(maxLength = 1000)
            .randomSequence(0)
            .take(1000)
            .forEach {
                lengths += it.length
            }

        assertTrue(lengths.size > 100)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<String>()

        Generator.strings().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}

class NonEmptyStringGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator.nonEmptyStrings()

    @Test
    fun generateStringsOfGivenLengthRange() {
        val values = Generator.nonEmptyStrings(maxLength = 12).randomSequence(0).take(200)

        assertTrue(values.all { it.length in 1..12 })
    }

    @Test
    fun doesNotGenerateEmpty() {
        assertTrue(Generator.nonEmptyStrings().randomSequence(0).take(1000).none { it.isEmpty() })
    }

    @Test
    fun generateOfManyLength() {
        val lengths = mutableSetOf<Int>()

        Generator.nonEmptyStrings(maxLength = 1000)
            .randomSequence(0)
            .take(1000)
            .forEach {
                lengths += it.length
            }

        assertTrue(lengths.size > 100)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<String>()

        Generator.nonEmptyStrings().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}

class NonBlankStringGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator.nonBlankStrings()

    @Test
    fun generateStringsOfGivenLengthRange() {
        val values = Generator.nonBlankStrings(maxLength = 12).randomSequence(0).take(200)

        assertTrue(values.all { it.length in 1..12 })
    }

    @Test
    fun doesNotGenerateBlank() {
        assertTrue(Generator.nonBlankStrings().randomSequence(0).take(1000).none { it.isBlank() })
    }

    @Test
    fun generateOfManyLength() {
        val lengths = mutableSetOf<Int>()

        Generator.nonBlankStrings(maxLength = 1000)
            .randomSequence(0)
            .take(1000)
            .forEach {
                lengths += it.length
            }

        assertTrue(lengths.size > 100)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<String>()

        Generator.nonBlankStrings().randomSequence(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
