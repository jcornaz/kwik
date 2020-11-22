package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.Falsification

/**
 * Exception thrown when a property is falsified
 *
 * @property attempts Number of attempts after which the falsification was found
 * @property iterations Number of iteration that was requested
 * @property seed Seed used for the random generation
 * @property arguments Argument list that cause a falsification
 * @property cause Error thrown by the system under test (if any)
 */
public data class FalsifiedPropertyError(
    public val attempts: Int,
    public val iterations: Int,
    public val seed: Long,
    public val arguments: List<Any?>,
    public val falsification: Falsification
) : AssertionError(buildString {
    appendLine("Property falsified after $attempts tests (out of $iterations)")

    arguments.forEachIndexed { index, arg ->
        appendLine("Argument ${index + 1}: $arg")
    }

    append("Generation seed: $seed")

    if (falsification is Falsification.UnexpectedResult) {
        append('\n')
        appendLine("Expected: ${falsification.expected}")
        append("Actual: ${falsification.actual}")
    }
}) {

    override val cause: Throwable? get() = (falsification as? Falsification.Error)?.throwable

    public constructor(
        attempts: Int,
        iterations: Int,
        seed: Long,
        arguments: List<Any?>,
        cause: Throwable
    ) : this(attempts, iterations, seed, arguments, Falsification.Error(cause))
}
