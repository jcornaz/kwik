package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CharacterGeneratorTest : AbstractGeneratorTest() {

    override val generator: Generator<Char> = Generator.characters()

    @Test
    fun generateSpace() {
        assertTrue(generator.randomSequence(0).take(5).any { it == ' ' })
    }

    @Test
    fun failIfEmptyCharSet() {
        assertFailsWith<IllegalArgumentException> { Generator.characters(charset = emptySet()) }
    }

    @Test
    fun failIfEntireCharsetExcluded() {
        assertFailsWith<IllegalArgumentException> {
            Generator.characters(
                charset = CharSets.printable,
                exclude = CharSets.printable
            )
        }
    }

    @Test
    fun generateDifferentValues() {
        val values = mutableSetOf<Char>()

        Generator.characters(CharSets.alphaNum)
            .randomSequence(0)
            .take(200)
            .forEach {
                values += it
            }

        assertTrue { values.size > 10 }
    }

    @Test
    fun dontGenerateExcludedChars() {
        val values = Generator.characters(exclude = setOf('a', 'b', 'c'))
            .randomSequence(0)
            .take(100)

        assertTrue(values.none { it == 'a' || it == 'b' || it == 'c' })
    }
}
