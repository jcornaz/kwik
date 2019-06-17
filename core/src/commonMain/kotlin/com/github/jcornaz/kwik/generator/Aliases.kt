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
