package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filterNot
import com.github.jcornaz.kwik.generator.api.withSamples

/**
 * Returns a generator of String of length between [minLength] and [maxLength] (inclusive)
 *
 * @param charset Set of character to be used in generated strings
 * @param exclude Characters to exclude from generated strings
 */
fun Generator.Companion.strings(
    minLength: Int = 0,
    maxLength: Int = maxOf(minLength, KWIK_DEFAULT_MAX_SIZE),
    charset: Set<Char> = StringCharSets.printable,
    exclude: Set<Char> = emptySet()
): Generator<String> {
    require(minLength >= 0) { "Invalid minLength: $minLength" }
    require(maxLength >= minLength) { "Invalid maxLength: $minLength (minLength=$minLength)" }

    val characters = (charset - exclude).toList()

    val generator = create { rng ->
        String(CharArray(rng.nextInt(minLength, maxLength + 1)) { characters.random(rng) })
    }

    val samples = mutableListOf<String>()

    if (minLength == 0)
        samples += ""

    if (' ' in characters && minLength <= 1 && maxLength >= 1)
        samples += " "

    return generator.withSamples(samples)
}

/**
 * Returns a generator of String of length between 1 and [maxLength] (inclusive)
 *
 * @param charset Set of character to be used in generated strings
 * @param exclude Characters to exclude from generated strings
 */
fun Generator.Companion.nonEmptyStrings(
    maxLength: Int = KWIK_DEFAULT_MAX_SIZE,
    charset: Set<Char> = StringCharSets.printable,
    exclude: Set<Char> = emptySet()
): Generator<String> = strings(1, maxLength, charset, exclude)

/**
 * Returns a generator of non-blank String of length between 1 and [maxLength] (inclusive)
 *
 * @param charset Set of character to be used in generated strings
 * @param exclude Characters to exclude from generated strings
 */
fun Generator.Companion.nonBlankStrings(
    maxLength: Int = KWIK_DEFAULT_MAX_SIZE,
    charset: Set<Char> = StringCharSets.printable,
    exclude: Set<Char> = emptySet()
): Generator<String> = nonEmptyStrings(maxLength, charset, exclude).filterNot { it.isBlank() }

/**
 * Common set of character to be used with string generators
 */
object StringCharSets {

    /** All printable characters */
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
