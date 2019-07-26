package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withNull

/**
 * Returns default [Generator] for the type [T].
 *
 * Throws an [IllegalArgumentException] if no generator is available for the requested type.
 *
 * The returned generator never returns null. You might consider call [withNull] if the requested type is nullable.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Generator.Companion.default(): Generator<T> = when (T::class) {
    Int::class -> ints() as Generator<T>
    Long::class -> longs() as Generator<T>
    Float::class -> floats() as Generator<T>
    Double::class -> doubles() as Generator<T>
    Boolean::class -> booleans() as Generator<T>
    String::class -> strings() as Generator<T>
    Any::class -> create { Any() } as Generator<T>
    else -> throw IllegalArgumentException("No default generator for ${T::class}")
}
