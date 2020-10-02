package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withSamples
import kotlin.random.Random

/**
 * Returns a generator of [Char]
 *
 * @param charset Set of character to be used
 * @param exclude Characters to exclude
 */
fun Generator.Companion.characters(
    charset: Set<Char> = CharSets.printable,
    exclude: Set<Char> = emptySet()
): Generator<Char> {
    val characters = (charset - exclude).toList()
    require(characters.isNotEmpty())

    val samples = mutableListOf<Char>()

    if (' ' in characters)
        samples += ' '

    return Generator { rng: Random -> characters.random(rng) }.withSamples(samples)
}

/**
 * Common set of character to be used with generators
 */
object CharSets {

    /** All printable characters */
    @Suppress("MagicNumber")
    val printable: Set<Char> = (32..127).mapTo(HashSet()) { it.toChar() }

    /** Numeric characters (0-9) */
    val numeric: Set<Char> = ('0'..'9').toHashSet()

    /** Lowercase alphabetic characters */
    val alphaLowerCase: Set<Char> = ('a'..'z').toHashSet()

    /** Uppercase alphabetic characters */
    val alphaUpperCase: Set<Char> = ('A'..'Z').toHashSet()

    /** Alphabetic characters (lower and upper cases)*/
    val alpha: Set<Char> = alphaLowerCase + alphaUpperCase

    /**
     * Alphabetic and numeric characters
     *
     * Equivalent of [alpha] + [numeric]
     */
    val alphaNum: Set<Char> = alpha + numeric
}
