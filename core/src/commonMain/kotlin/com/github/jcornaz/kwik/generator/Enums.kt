package com.github.jcornaz.kwik.generator

import com.github.jcornaz.kwik.Generator

/**
 * Returns a [Generator] emitting random enum instance picked in the enum [T]
 */
inline fun <reified T : Enum<T>> Generator.Companion.enum(): Generator<T> =
    of(*enumValues())
