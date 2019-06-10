package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import kotlin.test.Test
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
        assertTrue(Generator.strings(minLength= 1).samples.none { it.isEmpty() })
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
