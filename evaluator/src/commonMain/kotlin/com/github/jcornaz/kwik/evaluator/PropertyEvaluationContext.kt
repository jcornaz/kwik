package com.github.jcornaz.kwik.evaluator

/**
 * Context in which property are evaluated
 */
interface PropertyEvaluationContext {

    /**
     * Skip this evaluation if [condition] is true.
     *
     * Use this with caution as it can slow down the tests. The probability of the [condition] being true should be small.
     */
    fun skipIf(condition: Boolean)

    /**
     * Ensure that the [condition] is satisfied at least one time in every property assertion.
     */
    fun ensureAtLeastOne(condition: () -> Boolean): Boolean
}
