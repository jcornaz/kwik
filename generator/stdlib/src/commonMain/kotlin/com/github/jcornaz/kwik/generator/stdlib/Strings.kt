@file:Suppress("MatchingDeclarationName")

package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filterNot
import com.github.jcornaz.kwik.generator.api.withSamples
import kotlin.random.Random

/**
 * Returns a generator of String of length between [minLength] and [maxLength] (inclusive)
 *
 * @param charset Set of character to be used in generated strings
 * @param exclude Characters to exclude from generated strings
 */
fun Generator.Companion.strings(
    minLength: Int = 0,
    maxLength: Int = maxOf(minLength, KWIK_DEFAULT_MAX_SIZE),
    charset: Set<Char> = CharSets.printable,
    exclude: Set<Char> = emptySet()
) = strings(minLength, maxLength, characters(charset, exclude))

/**
 * Returns a generator of String of length between [minLength] and [maxLength] (inclusive)
 *
 * @param charGenerator A generator of characters which will be used to construct the strings
 */
fun Generator.Companion.strings(
    minLength: Int = 0,
    maxLength: Int = maxOf(minLength, KWIK_DEFAULT_MAX_SIZE),
    charGenerator: Generator<Char> = Generator.characters(),
): Generator<String> {
    require(minLength >= 0) { "Invalid minLength: $minLength" }
    require(maxLength >= minLength) { "Invalid maxLength: $minLength (minLength=$minLength)" }

    val generator = Generator { rng: Random ->
        CharArray(rng.nextInt(minLength, maxLength + 1)) { charGenerator.generate(rng) }.concatToString()
    }

    val samples = mutableListOf<String>()

    if (minLength == 0)
        samples += ""

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
    charset: Set<Char> = CharSets.printable,
    exclude: Set<Char> = emptySet()
): Generator<String> = nonEmptyStrings(maxLength, characters(charset, exclude))

/**
 * Returns a generator of String of length between 1 and [maxLength] (inclusive)
 *
 * @param charGenerator A generator of characters which will be used to construct the strings
 */
fun Generator.Companion.nonEmptyStrings(
    maxLength: Int = KWIK_DEFAULT_MAX_SIZE,
    charGenerator: Generator<Char> = Generator.characters(),
): Generator<String> = strings(1, maxLength, charGenerator)


/**
 * Returns a generator of non-blank String of length between 1 and [maxLength] (inclusive)
 *
 * @param charset Set of character to be used in generated strings
 * @param exclude Characters to exclude from generated strings
 */
fun Generator.Companion.nonBlankStrings(
    maxLength: Int = KWIK_DEFAULT_MAX_SIZE,
    charset: Set<Char> = CharSets.printable,
    exclude: Set<Char> = emptySet()
): Generator<String> = nonEmptyStrings(maxLength, characters(charset, exclude)).filterNot { it.isBlank() }

/**
 * Returns a generator of non-blank String of length between 1 and [maxLength] (inclusive)
 *
 * @param charGenerator A generator of characters which will be used to construct the strings
 */
fun Generator.Companion.nonBlankStrings(
    maxLength: Int = KWIK_DEFAULT_MAX_SIZE,
    charGenerator: Generator<Char> = Generator.characters(),
): Generator<String> = nonEmptyStrings(maxLength, charGenerator).filterNot { it.isBlank() }


/**
 * Common set of character to be used with string generators
 */
@Deprecated("Replaced by CharSets", replaceWith = ReplaceWith("CharSets"))
object StringCharSets {

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
