package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator

internal fun <T> Generator<T>.shrink(initialValue: T, property: (T) -> Boolean): T = initialValue
