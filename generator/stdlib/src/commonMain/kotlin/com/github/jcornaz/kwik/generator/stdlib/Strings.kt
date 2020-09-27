@file:Suppress("MatchingDeclarationName")

package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filterNot
import com.github.jcornaz.kwik.generator.api.withSamples
import kotlin.random.Random

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
 * @param charGenerator A generator of characters which will be used to construct the strings
 */
fun Generator.Companion.nonEmptyStrings(
    maxLength: Int = KWIK_DEFAULT_MAX_SIZE,
    charGenerator: Generator<Char> = Generator.characters(),
): Generator<String> = strings(1, maxLength, charGenerator)

/**
 * Returns a generator of non-blank String of length between 1 and [maxLength] (inclusive)
 *
 * @param charGenerator A generator of characters which will be used to construct the strings
 */
fun Generator.Companion.nonBlankStrings(
    maxLength: Int = KWIK_DEFAULT_MAX_SIZE,
    charGenerator: Generator<Char> = Generator.characters(),
): Generator<String> = nonEmptyStrings(maxLength, charGenerator).filterNot { it.isBlank() }

