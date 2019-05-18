package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.withSamples

@Suppress("MagicNumber")
private val PRINTABLE_CHARACTERS = (32..127).map { it.toChar() }.toSet()

/**
 * Returns a generator of String of length between [minLength] and [maxLength] (inclusive)
 *
 * @param charset Set of character to be used in generated strings
 * @param exclude Characters to exclude from generated strings
 */
fun Generator.Companion.strings(
    minLength: Int = DEFAULT_MIN_SIZE,
    maxLength: Int = DEFAULT_MAX_SIZE,
    charset: Set<Char> = PRINTABLE_CHARACTERS,
    exclude: Set<Char> = emptySet()
): Generator<String> {
    require(minLength >= 0) { "Invalid minLength: $minLength" }
    require(maxLength >= minLength) { "Invalid maxLength: $minLength (minLength=$minLength)" }

    val characters = (charset - exclude).toList()

    val generator = create { rng ->
        String(CharArray(rng.nextSize(minLength, maxLength)) { characters.random(rng) })
    }

    val samples = listOf("", " ").filter { string ->
        string.length in minLength..maxLength && string.all { it in characters }
    }

    return if (samples.isEmpty()) generator else generator.withSamples(samples)
}
