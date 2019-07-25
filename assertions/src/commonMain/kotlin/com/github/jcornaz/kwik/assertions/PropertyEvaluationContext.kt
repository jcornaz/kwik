package com.github.jcornaz.kwik.assertions

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
}
