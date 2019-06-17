package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.filterNot
import com.github.jcornaz.kwik.withSamples

@Suppress("MagicNumber", "TopLevelPropertyNaming")
private val PRINTABLE_CHARACTERS = (32..127).map { it.toChar() }.toSet()

/**
 * Returns a generator of String of length between [minLength] and [maxLength] (inclusive)
 *
 * @param charset Set of character to be used in generated strings
 * @param exclude Characters to exclude from generated strings
 */
fun Generator.Companion.strings(
    minLength: Int = DEFAULT_MIN_SIZE,
    maxLength: Int = maxOf(minLength, DEFAULT_MAX_SIZE),
    charset: Set<Char> = PRINTABLE_CHARACTERS,
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

fun Generator.Companion.nonEmptyStrings(
    maxLength: Int = DEFAULT_MAX_SIZE,
    charset: Set<Char> = PRINTABLE_CHARACTERS,
    exclude: Set<Char> = emptySet()
): Generator<String> = strings(1, maxLength, charset, exclude)

fun Generator.Companion.nonBlankStrings(
    maxLength: Int = DEFAULT_MAX_SIZE,
    charset: Set<Char> = PRINTABLE_CHARACTERS,
    exclude: Set<Char> = emptySet()
): Generator<String> = nonEmptyStrings(maxLength, charset, exclude).filterNot { it.isBlank() }
