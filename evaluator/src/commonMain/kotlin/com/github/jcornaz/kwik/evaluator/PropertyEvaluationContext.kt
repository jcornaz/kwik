package com.github.jcornaz.kwik.evaluator

/**
 * Context in which property are evaluated
 */
public interface PropertyEvaluationContext {

    /**
     * Skip this evaluation if [condition] is true.
     *
     * Use this with caution as it can slow down the tests.
     * The probability of the [condition] being true should be small.
     */
    public fun skipIf(condition: Boolean)

    /**
     * Force to evaluate the property has many time as necessary
     * so that the [predicate] evaluate to `true` at least once.
     */
    public fun ensureAtLeastOne(predicate: () -> Boolean)
}
