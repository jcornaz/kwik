package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator

internal fun <T> Generator<T>.shrink(initialValue: T, property: PropertyEvaluationContext.(T) -> Boolean): T {
    var lastSkipped: T? = null
    var skipped = false

    shrink(initialValue).forEach { value ->
        val evaluation = evaluate(value, property)
        if (evaluation == false) return shrink(value, property)

        if (evaluation == null) {
            lastSkipped = value
            skipped = true
        }
    }

    @Suppress("UNCHECKED_CAST")
    if (skipped) return shrink(lastSkipped as T, property)

    return initialValue
}

private fun <T> evaluate(value: T, property: PropertyEvaluationContext.(T) -> Boolean): Boolean? {
    return try {
        SkipExceptionContext.property(value)
    } catch (skip: SkipEvaluation) {
        null
    }
}
