package com.github.jcornaz.kwik.generator

import kotlin.random.Random

internal const val DEFAULT_MIN_SIZE = 0
internal const val DEFAULT_MAX_SIZE = 200

internal fun Random.nextSize(minSize: Int, maxSize: Int): Int =
    (nextDouble().let { it * it * it } * (maxSize - minSize)).toInt() + minSize
