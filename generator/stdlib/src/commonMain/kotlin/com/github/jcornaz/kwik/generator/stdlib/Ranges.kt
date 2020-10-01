@file:Suppress("EmptyRange")

package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withSamples


/**
 * Returns a generator of ClosedRange<T>, which uses the provided Generator for creating start and end elements
 *
 * @param <T> The type of the ClosedRange
 * @param elementGen A generator producing T, which will create the start and end elements of the range
 */
fun <T : Comparable<T>> Generator.Companion.ranges(elementGen: Generator<T>): Generator<ClosedRange<T>> =
    Generator { rng -> elementGen.generate(rng)..elementGen.generate(rng) }


/**
 * Creates a Generator for ClosedRange<Int>, includes samples for empty range and single item range
 *
 * @param elementGen A generator producing T, which will create the start and end elements of the range
 */
fun Generator.Companion.intRanges(elementGen: Generator<Int> = ints()): Generator<ClosedRange<Int>> =
    ranges(elementGen).withSamples(
        1..0,
        0..0
    )


/**
 * Creates a Generator for ClosedRange<Char>, includes samples for empty range and single item range
 *
 * @param elementGen A generator producing T, which will create the start and end elements of the range
 */
fun Generator.Companion.charRanges(elementGen: Generator<Char> = characters()): Generator<ClosedRange<Char>> =
    ranges(elementGen).withSamples(
        'B'..'A',
        'A'..'A'
    )


/**
 * Creates a Generator for ClosedRange<Long>, includes samples for empty range and single item range
 *
 * @param elementGen A generator producing T, which will create the start and end elements of the range
 */
fun Generator.Companion.longRanges(elementGen: Generator<Long> = longs()): Generator<ClosedRange<Long>> =
    ranges(elementGen).withSamples(
        1L..0L,
        0L..0L
    )

