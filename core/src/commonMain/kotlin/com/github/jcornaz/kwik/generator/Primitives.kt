package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.map
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

/**
 * Returns a generator of booleans
 */
fun Generator.Companion.booleans(): Generator<Boolean> = create { it.nextBoolean() }

/**
 * Returns a generator including [Double.NaN] in the samples
 */
fun Generator<Double>.withNaN(): Generator<Double> = withSamples(Double.NaN)

/**
 * Returns a generator including [Float.NaN] in the samples
 */
fun Generator<Float>.withNaN(): Generator<Float> = withSamples(Float.NaN)
