package com.github.jcornaz.kwik.generator

/**
 * Returns a [Generator] emitting random enum instance picked in the enum [T]
 */
@Suppress("SpreadOperator")
inline fun <reified T : Enum<T>> Generator.Companion.enum(): Generator<T> =
    of(*enumValues())
