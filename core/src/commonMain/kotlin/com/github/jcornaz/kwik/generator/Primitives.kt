package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.map
import com.github.jcornaz.kwik.plus
import com.github.jcornaz.kwik.withSamples
import kotlin.random.nextInt
import kotlin.random.nextLong

/**
 * Returns a generator of integer, all values being generated between [min] and [max] (inclusive)
 *
 * Contains the samples: -1, 0, 1, [min] and [max]
 */
fun Generator.Companion.ints(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Generator<Int> {
    require(max >= min) { "Max must be greater than min but min was $min and max was $max" }

    val range = min..max
    val samples = listOf(0, 1, -1, min, max).filter { it in range }

    return create { it.nextInt(range) }.withSamples(samples)
}

fun Generator.Companion.positiveInts(max: Int = Int.MAX_VALUE): Generator<Int> = ints(0, max)
fun Generator.Companion.negativeInts(min: Int = Int.MIN_VALUE): Generator<Int> = ints(min, -1)
fun Generator.Companion.naturalInts(max: Int = Int.MAX_VALUE): Generator<Int> = ints(1, max)

fun Generator.Companion.nonZeroInts(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Generator<Int> =
    negativeInts(min) + naturalInts(max)

/**
 * Returns a generator of longs, all values being generated between [min] and [max] (inclusive)
 *
 * Contains the samples: -1, 0, 1, [min] and [max]
 */
fun Generator.Companion.longs(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Generator<Long> {
    require(max >= min) { "Max must be greater than min but min was $min and max was $max" }

    val range = min..max
    val samples = listOf(0, 1, -1, min, max).filter { it in range }

    return create { it.nextLong(range) }.withSamples(samples)
}

fun Generator.Companion.positiveLongs(max: Long = Long.MAX_VALUE): Generator<Long> = longs(0L, max)
fun Generator.Companion.negativeLongs(min: Long = Long.MIN_VALUE): Generator<Long> = longs(min, -1L)
fun Generator.Companion.naturalLongs(max: Long = Long.MAX_VALUE): Generator<Long> = longs(1L, max)

fun Generator.Companion.nonZeroLongs(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Generator<Long> =
    negativeLongs(min) + naturalLongs(max)

/**
 * Returns a generator of floats, all values being generated between [min] and [max] (inclusive)
 *
 * Contains the samples: -1, 0, 1, [min] and [max]
 *
 * [min] and [max] must be finite.
 */
fun Generator.Companion.floats(
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE
): Generator<Float> = doubles(min.toDouble(), max.toDouble()).map { it.toFloat() }

fun Generator.Companion.positiveFloats(max: Float = Float.MAX_VALUE): Generator<Float> =
    floats(0f, max)

fun Generator.Companion.negativeFloats(min: Float = -Float.MAX_VALUE): Generator<Float> =
    floats(min, -Float.MIN_VALUE)

fun Generator.Companion.nonZeroFloats(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): Generator<Float> =
    negativeFloats(min) + floats(Float.MIN_VALUE, max)

/**
 * Returns a generator of floats, all values being generated between [min] and [max] (inclusive)
 *
 * Contains samples: -1, 0, 1, [min] and [max]
 *
 * [min] and [max] must be finite.
 */
fun Generator.Companion.doubles(
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE
): Generator<Double> {
    require(max >= min) { "Until must be greater than from but from was $min and until was $max" }
    require(min.isFinite() && max.isFinite()) { "from or until was not finite" }

    val range = min..max
    val samples = listOf(0.0, 1.0, -1.0, min, max).filter { it in range }

    val until = (max + Double.MIN_VALUE).takeIf { it.isFinite() } ?: max
    val generator = create { it.nextDouble(min, until) }

    return generator.withSamples(samples)
}

fun Generator.Companion.positiveDoubles(max: Double = Double.MAX_VALUE): Generator<Double> =
    doubles(0.0, max)

fun Generator.Companion.negativeDoubles(min: Double = -Double.MAX_VALUE): Generator<Double> =
    doubles(min, -Double.MIN_VALUE)

fun Generator.Companion.nonZeroDoubles(
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE
): Generator<Double> =
    negativeDoubles(min) + doubles(Double.MIN_VALUE, max)

/**
 * Returns a generator of booleans
 */
fun Generator.Companion.booleans(): Generator<Boolean> = create { it.nextBoolean() }

/**
 * Returns a generator including [Double.NaN] in the samples
 */
fun Generator<Double>.withNaN(): Generator<Double> = withSamples(Double.NaN)
