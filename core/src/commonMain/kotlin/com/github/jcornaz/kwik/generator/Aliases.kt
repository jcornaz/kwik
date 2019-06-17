package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.plus

fun Generator.Companion.positiveInts(max: Int = Int.MAX_VALUE): Generator<Int> = ints(0, max)
fun Generator.Companion.negativeInts(min: Int = Int.MIN_VALUE): Generator<Int> = ints(min, -1)
fun Generator.Companion.naturalInts(max: Int = Int.MAX_VALUE): Generator<Int> = ints(1, max)

fun Generator.Companion.nonZeroInts(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Generator<Int> =
    negativeInts(min) + naturalInts(max)

fun Generator.Companion.positiveLongs(max: Long = Long.MAX_VALUE): Generator<Long> = longs(0L, max)
fun Generator.Companion.negativeLongs(min: Long = Long.MIN_VALUE): Generator<Long> = longs(min, -1L)
fun Generator.Companion.naturalLongs(max: Long = Long.MAX_VALUE): Generator<Long> = longs(1L, max)

fun Generator.Companion.nonZeroLongs(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Generator<Long> =
    negativeLongs(min) + naturalLongs(max)

fun Generator.Companion.positiveDoubles(max: Double = Double.MAX_VALUE): Generator<Double> =
    doubles(0.0, max)

fun Generator.Companion.negativeDoubles(min: Double = -Double.MAX_VALUE): Generator<Double> =
    doubles(min, -Double.MIN_VALUE)

fun Generator.Companion.nonZeroDoubles(
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE
): Generator<Double> =
    negativeDoubles(min) + doubles(Double.MIN_VALUE, max)

fun Generator.Companion.positiveFloats(max: Float = Float.MAX_VALUE): Generator<Float> =
    floats(0f, max)

fun Generator.Companion.negativeFloats(min: Float = -Float.MAX_VALUE): Generator<Float> =
    floats(min, -Float.MIN_VALUE)

fun Generator.Companion.nonZeroFloats(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): Generator<Float> =
    negativeFloats(min) + floats(Float.MIN_VALUE, max)
