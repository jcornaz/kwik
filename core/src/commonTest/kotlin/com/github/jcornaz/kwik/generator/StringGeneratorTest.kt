package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StringGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator.strings()

    @Test
    fun generateStringsOfGivenLengthRange() {
        val values = Generator.strings(minLength = 3, maxLength = 12).randoms(0).take(200)

        assertTrue(values.all { it.length in 3..12 })
    }

    @Test
    fun samplesContainsEmpty() {
        assertTrue(Generator.strings().samples.any { it.isEmpty() })
    }

    @Test
    fun samplesContainsBlank() {
        assertTrue(Generator.strings().samples.any { it.isNotEmpty() && it.isBlank() })
    }

    @Test
    fun noEmptySampleWhenMinLengthIsGreaterThan0() {
        assertTrue(Generator.strings(minLength = 1).samples.none { it.isEmpty() })
    }

    @Test
    fun sampleSizeIsBiggerThanMinLength() {
        assertTrue(Generator.strings(minLength = 2).samples.none { it.length <= 1 })
    }

    @Test
    fun dontGenerateExcludedChars() {
        val values = Generator.strings(exclude = setOf('a', 'b', 'c')).randoms(0).take(1000)

        assertTrue(values.none { string -> string.any { it == 'a' || it == 'b' || it == 'c' } })
    }

    @Test
    fun bigMinLengthIsPossible() {
        val generator = Generator.strings(minLength = 1000)

        assertEquals(1000, generator.randoms(1).first().length)
    }

    @Test
    fun generateOfManyLength() {
        val lengths = mutableSetOf<Int>()

        Generator.strings(maxLength = 1000)
            .randoms(0)
            .take(1000)
            .forEach {
                lengths += it.length
            }

        assertTrue(lengths.size > 100)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<String>()

        Generator.strings().randoms(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}

class NonEmptyStringGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator.nonEmptyStrings()

    @Test
    fun generateStringsOfGivenLengthRange() {
        val values = Generator.nonEmptyStrings(maxLength = 12).randoms(0).take(200)

        assertTrue(values.all { it.length in 1..12 })
    }

    @Test
    fun doesNotGenerateEmpty() {
        assertTrue(Generator.nonEmptyStrings().randoms(0).take(1000).none { it.isEmpty() })
    }

    @Test
    fun samplesDoesNotContainsEmpty() {
        assertTrue(Generator.nonEmptyStrings().samples.none { it.isEmpty() })
    }

    @Test
    fun samplesContainsBlank() {
        assertTrue(Generator.nonEmptyStrings().samples.any { it.isNotEmpty() && it.isBlank() })
    }

    @Test
    fun dontGenerateExcludedChars() {
        val values = Generator.nonEmptyStrings(exclude = setOf('a', 'b', 'c')).randoms(0).take(1000)

        assertTrue(values.none { string -> string.any { it == 'a' || it == 'b' || it == 'c' } })
    }

    @Test
    fun generateOfManyLength() {
        val lengths = mutableSetOf<Int>()

        Generator.nonEmptyStrings(maxLength = 1000)
            .randoms(0)
            .take(1000)
            .forEach {
                lengths += it.length
            }

        assertTrue(lengths.size > 100)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<String>()

        Generator.nonEmptyStrings().randoms(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}

class NonBlankStringGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator.nonBlankStrings()

    @Test
    fun generateStringsOfGivenLengthRange() {
        val values = Generator.nonBlankStrings(maxLength = 12).randoms(0).take(200)

        assertTrue(values.all { it.length in 1..12 })
    }

    @Test
    fun doesNotGenerateBlank() {
        assertTrue(Generator.nonBlankStrings().randoms(0).take(1000).none { it.isBlank() })
    }

    @Test
    fun samplesDoesNotContainBlank() {
        assertTrue(Generator.nonBlankStrings().samples.none { it.isBlank() })
    }

    @Test
    fun dontGenerateExcludedChars() {
        val values = Generator.nonBlankStrings(exclude = setOf('a', 'b', 'c')).randoms(0).take(1000)

        assertTrue(values.none { string -> string.any { it == 'a' || it == 'b' || it == 'c' } })
    }

    @Test
    fun generateOfManyLength() {
        val lengths = mutableSetOf<Int>()

        Generator.nonBlankStrings(maxLength = 1000)
            .randoms(0)
            .take(1000)
            .forEach {
                lengths += it.length
            }

        assertTrue(lengths.size > 100)
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<String>()

        Generator.nonBlankStrings().randoms(0).take(200).forEach {
            values += it
        }

        assertTrue(values.size > 100)
    }
}
