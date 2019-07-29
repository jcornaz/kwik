package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator

internal fun <T> Generator<T>.shrink(initialValue: T, property: (T) -> Boolean): T {
    var result = initialValue

    var smallerValues = shrink(result)
    var index = smallerValues.indexOfFirst { !property(it) }
    while (index >= 0) {
        result = smallerValues[index]
        smallerValues = shrink(result)
        index = smallerValues.indexOfFirst { !property(it) }
    }

    return result
}
