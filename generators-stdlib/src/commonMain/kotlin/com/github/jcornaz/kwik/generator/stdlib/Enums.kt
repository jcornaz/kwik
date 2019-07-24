package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.Generator

/**
 * Returns a [Generator] emitting random enum instance picked in the enum [T]
 */
@Suppress("SpreadOperator")
inline fun <reified T : Enum<T>> Generator.Companion.enum(): Generator<T> =
    of(*enumValues())
