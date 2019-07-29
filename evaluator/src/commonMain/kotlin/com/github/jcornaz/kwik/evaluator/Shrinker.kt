package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator

internal fun <T> Generator<T>.shrink(initialValue: T, property: PropertyEvaluationContext.(T) -> Boolean): T {
    var lastSkipped: T? = null
    var hasSkippedValue = false

    shrink(initialValue).forEach { value ->
        val evaluation = evaluate(value, property)
        if (evaluation == false) return shrink(value, property)

        if (evaluation == null) {
            lastSkipped = value
            hasSkippedValue = true
        }
    }

    @Suppress("UNCHECKED_CAST")
    return if (hasSkippedValue) shrink(lastSkipped as T, property) else initialValue
}

private fun <T> evaluate(value: T, property: PropertyEvaluationContext.(T) -> Boolean): Boolean? {
    return try {
        SkipExceptionContext.property(value)
    } catch (skip: SkipEvaluation) {
        null
    }
}
