package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.frequency
import com.github.jcornaz.kwik.generator.api.map
import com.github.jcornaz.kwik.generator.api.plus
import com.github.jcornaz.kwik.generator.api.withSamples
import kotlin.random.nextInt
import kotlin.random.nextLong

/**
 * Returns a generator of integers, all values being generated between [min] and [max] (inclusive)
 *
 * Contains the samples: -1, 0, 1, [min] and [max]
 */
fun Generator.Companion.ints(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Generator<Int> {
    require(max >= min) { "Max must be greater than min but min was $min and max was $max" }

    val range = min..max
    val samples = listOf(0, 1, -1, min, max).filter { it in range }

    return create { it.nextInt(range) }.withSamples(samples, probability = 0.3)
}

/**
 * Returns a generator of positive integers, all values being generated between 0 and [max] (inclusive)
 *
 * Contains the samples: 0, 1 and [max]
 */
fun Generator.Companion.positiveInts(max: Int = Int.MAX_VALUE): Generator<Int> = ints(0, max)

/**
 * Returns a generator of negative integers, all values being generated between [min] and -1 (inclusive)
 *
 * Contains the samples: -1 and [min]
 */
fun Generator.Companion.negativeInts(min: Int = Int.MIN_VALUE): Generator<Int> = ints(min, -1)

/**
 * Returns a generator of natural integers, all values being generated between 1 and [max] (inclusive)
 *
 * Contains the samples: 1 and [max]
 */
fun Generator.Companion.naturalInts(max: Int = Int.MAX_VALUE): Generator<Int> = ints(1, max)

/**
 * Returns a generator of non-zero integers, all values being generated between [min] and [max] (inclusive)
 *
 * 0 is never generated
 *
 * Contains the samples: -1, 1, [min] and [max]
 */
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

    return create { it.nextLong(range) }.withSamples(samples, probability = 0.3)
}

/**
 * Returns a generator of positive longs, all values being generated between 0 and [max] (inclusive)
 *
 * Contains the samples: 0, 1 and [max]
 */
fun Generator.Companion.positiveLongs(max: Long = Long.MAX_VALUE): Generator<Long> = longs(0L, max)

/**
 * Returns a generator of negative longs, all values being generated between [min] and -1 (inclusive)
 *
 * Contains the samples: -1 and [min]
 */
fun Generator.Companion.negativeLongs(min: Long = Long.MIN_VALUE): Generator<Long> = longs(min, -1L)

/**
 * Returns a generator of natural longs, all values being generated between 1 and [max] (inclusive)
 *
 * Contains the samples: 1 and [max]
 */
fun Generator.Companion.naturalLongs(max: Long = Long.MAX_VALUE): Generator<Long> = longs(1L, max)

/**
 * Returns a generator of non-zero longs, all values being generated between [min] and [max] (inclusive)
 *
 * 0 is never generated
 *
 * Contains the samples: -1, 1, [min] and [max]
 */
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

/**
 * Returns a generator of positive floats, all values being generated between 0.0 and [max] (inclusive)
 *
 * Contains the samples: 0.0, 1.0 and [max]
 */
fun Generator.Companion.positiveFloats(max: Float = Float.MAX_VALUE): Generator<Float> =
    floats(0f, max)

/**
 * Returns a generator of negative floats, all values being generated between [min] and 0.0 (exclusive)
 *
 * Contains the samples: -1.0 and [min]
 */
fun Generator.Companion.negativeFloats(min: Float = -Float.MAX_VALUE): Generator<Float> =
    floats(min, -Float.MIN_VALUE)

/**
 * Returns a generator of non-zero floats, all values being generated between [min] and [max] (inclusive)
 *
 * 0.0 is never generated
 *
 * Contains the samples: -1, 1, [min] and [max]
 */
fun Generator.Companion.nonZeroFloats(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): Generator<Float> =
    negativeFloats(min) + floats(Float.MIN_VALUE, max)

/**
 * Returns a generator of floats, all values being generated between [min] and [max] (inclusive)
 *
 * Contains samples: -1, 0, 1, [min] and [max]
 *
 * [min] and [max] must be finite.
 */
@Suppress("MagicNumber")
fun Generator.Companion.doubles(
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE
): Generator<Double> {
    require(max >= min) { "until must be greater than from, but from was $min and until was $max" }
    require(min.isFinite() && max.isFinite()) { "from or until was not finite" }

    val range = min..max

    val until = (max + Double.MIN_VALUE).takeIf { it.isFinite() } ?: max

    val generators = mutableListOf(
        0.3 to create { it.nextDouble(min, until) },
        0.2 to create { it.nextDouble(-100.0, 100.0).coerceIn(min, max) },
        0.1 to of(min, max)
    )

    if (0.0 in range)
        generators += 0.1 to of(0.0)

    if (-1.0 in range)
        generators += 0.1 to of(-1.0)

    if (1.0 in range)
        generators += 0.1 to of(1.0)

    if (min <= 1.0 && max >= 0.0)
        generators += 0.1 to create { it.nextDouble().coerceIn(min, max) }

    return frequency(generators)
}

/**
 * Returns a generator of positive doubles, all values being generated between 0.0 and [max] (inclusive)
 *
 * Contains the samples: 0.0, 1.0 and [max]
 */
fun Generator.Companion.positiveDoubles(max: Double = Double.MAX_VALUE): Generator<Double> =
    doubles(0.0, max)

/**
 * Returns a generator of negative doubles, all values being generated between [min] and 0.0 (exclusive)
 *
 * Contains the samples: -1.0 and [min]
 */
fun Generator.Companion.negativeDoubles(min: Double = -Double.MAX_VALUE): Generator<Double> =
    doubles(min, -Double.MIN_VALUE)

/**
 * Returns a generator of non-zero doubles, all values being generated between [min] and [max] (inclusive)
 *
 * 0.0 is never generated
 *
 * Contains the samples: -1, 1, [min] and [max]
 */
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
