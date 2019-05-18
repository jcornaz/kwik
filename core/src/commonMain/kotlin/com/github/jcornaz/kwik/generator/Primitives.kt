package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.withSamples
import kotlin.random.nextInt
import kotlin.random.nextLong

fun Generator.Companion.ints(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Generator<Int> {
    require(max > min) { "Max must be greater than min but min was $min and max was $max" }

    val range = min..max
    val samples = listOf(0, 1, -1, min, max).filter { it in range }

    return create { it.nextInt(range) }.withSamples(samples)
}

fun Generator.Companion.longs(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Generator<Long> {
    require(max > min) { "Max must be greater than min but min was $min and max was $max" }

    val range = min..max
    val samples = listOf(0, 1, -1, min, max).filter { it in range }

    return create { it.nextLong(range) }.withSamples(samples)
}

fun Generator.Companion.floats(
    from: Float = Float.NEGATIVE_INFINITY,
    until: Float = Float.POSITIVE_INFINITY
): Generator<Float> {
    require(until > from) { "Until must be greater than from but from was $from and until was $until" }

    val range = from..until
    val samples = listOf(0f, 1f, -1f, from).filter { it in range }

    return create { it.nextFloat() * (until - from) + from }.withSamples(samples)
}

fun Generator.Companion.doubles(
    from: Double = Double.NEGATIVE_INFINITY,
    until: Double = Double.POSITIVE_INFINITY
): Generator<Double> {
    require(until > from) { "Until must be greater than from but from was $from and until was $until" }

    val range = from..until
    val samples = listOf(0.0, 1.0, -1.0, from).filter { it in range }

    return create { it.nextDouble(from, until) }.withSamples(samples)
}

fun Generator.Companion.booleans(): Generator<Boolean> = create { it.nextBoolean() }
