package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.TestResult
import com.github.jcornaz.kwik.fuzzer.api.Fuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.findSimplestFalsification
import com.github.jcornaz.kwik.generator.api.randomSequence


/**
 * Call multiple times [block] with random values generated by the given [fuzzer]
 *
 * The [block] must perform assertions and throw an exception if the property is falsified.
 * Absence of exception thrown in the [block] means the property is satisfied.
 *
 * @param iterations Number of times [block] should be executed
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param block Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must return a throw an exception if the property is falsified.
 */
@ExperimentalKwikApi
public fun <T> forAny(
    fuzzer: Fuzzer<T>,
    iterations: Int = kwikDefaultIterations,
    seed: Long = nextSeed(),
    block: (T) -> TestResult
) {
    require(iterations > 0) { "Iterations must be > 0, but was: $iterations" }

    val inputIterator = fuzzer.generator.randomSequence(seed).iterator()
    val unsatisfiedGuarantees = fuzzer.guarantees.toMutableList()
    var iterationDone = 0

    do {
        val input = inputIterator.next()

        unsatisfiedGuarantees.removeSatisfying(input)

        when (val testResult = safeTest(input, block)) {
            TestResult.Skip -> Unit
            TestResult.Satisfied -> ++iterationDone
            is TestResult.Falsified, is TestResult.Error ->
                fuzzer.simplifier.simplifyAndThrow(input, block, iterationDone, iterations, seed, testResult)
        }
    } while (iterationDone < iterations || unsatisfiedGuarantees.isNotEmpty())
}

@ExperimentalKwikApi
private fun <T> Simplifier<T>.simplifyAndThrow(
    input: T,
    block: (T) -> TestResult,
    iterationDone: Int,
    iterations: Int,
    seed: Long,
    testResult: TestResult
) {
    val simplerInput = findSimplestFalsification(input) { safeTest(it, block) is TestResult.Satisfied }
    throw FalsifiedPropertyError(
        attempts = iterationDone + 1,
        iterations = iterations,
        seed = seed,
        arguments = listOf(simplerInput),
        falsification = when (testResult) {
            is TestResult.Falsified -> Falsification.Result(testResult)
            is TestResult.Error -> Falsification.Error(testResult.cause)
            else -> error("$testResult was not a failure")
        }
    )
}

private fun <T> safeTest(input: T, block: (T) -> TestResult): TestResult =
    try {
        block(input)
    } catch (t: Throwable) {
        TestResult.Error(t)
    }

private fun <T> MutableList<(T) -> Boolean>.removeSatisfying(input: T) {
    val iterator = listIterator()
    while (iterator.hasNext()) {
        if (iterator.next().invoke(input))
            iterator.remove()
    }
}
