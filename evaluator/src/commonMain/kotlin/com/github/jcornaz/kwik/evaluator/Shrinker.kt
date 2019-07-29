package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator

internal fun <T> Generator<T>.shrink(initialValue: T, property: PropertyEvaluationContext.(T) -> Boolean): T {
    var result = initialValue

    var smallerValues = shrink(result)
    var index = smallerValues.indexOfFirst { it falsifies property }
    while (index >= 0) {
        result = smallerValues[index]
        smallerValues = shrink(result)
        index = smallerValues.indexOfFirst { it falsifies property }
    }

    return result
}

private infix fun <T> T.falsifies(property: PropertyEvaluationContext.(T) -> Boolean): Boolean {
    return try {
        !SkipExceptionContext.property(this)
    } catch (skip: SkipEvaluation) {
        false
    }
}
