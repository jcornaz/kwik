package com.github.jcornaz.kwik

/**
 * Represents the result of a single evaluation of a property test
 *
 * Can be [TestResult.Discard], [TestResult.Satisfied] or [TestResult.Falsified].
 */
public sealed class TestResult {

    /**
     * The evaluation is not relevant to know if the property is held or not.
     *
     * It will be discarded and a new test evaluation will be performed.
     */
    public object Discard : TestResult()

    /**
     * The property was held (satisfied) in this evaluation.
     *
     * The evaluator will decide whether or not it is necessary to proceed with more tests.
     */
    public object Satisfied : TestResult()

    /**
     * The property is falsified. [falsification] contains more details.
     *
     * The property test can immediately result in failure.
     */
    public data class Falsified(val falsification: Falsification) : TestResult()
}

/**
 * Represents a falsification of the property.
 * It contains information that may help to understand the cause of the test failure.
 */
public sealed class Falsification {

    /** The [throwable] has been thrown during the property evaluation */
    public data class Error(val throwable: Throwable) : Falsification()

    /** There was no exception, but the [actual] result doesn't comply with the [expected] rule */
    public data class UnexpectedResult(val expected: String, val actual: String) : Falsification()
}

/** Returns [TestResult.Satisfied] if this is true, and [TestResult.Falsified] otherwise */
public fun Boolean.alwaysTrue(): TestResult = this alwaysEquals true

/** Returns [TestResult.Satisfied] if this is false, and [TestResult.Falsified] otherwise */
public fun Boolean.neverTrue(): TestResult = this alwaysEquals false

/** Returns [TestResult.Satisfied] if this is false, and [TestResult.Falsified] otherwise */
public fun Boolean.alwaysFalse(): TestResult = this alwaysEquals false

/** Returns [TestResult.Satisfied] if this is true, and [TestResult.Falsified] otherwise */
public fun Boolean.neverFalse(): TestResult = this alwaysEquals true

/** Returns [TestResult.Satisfied] if this is equal to [expected], and [TestResult.Falsified] otherwise */
public infix fun <T> T.alwaysEquals(expected: T): TestResult =
    if (this == expected) TestResult.Satisfied else {
        TestResult.Falsified(Falsification.UnexpectedResult(expected = expected.toString(), actual = toString()))
    }

/** Returns [TestResult.Satisfied] if this is NOT equal to [unexpected], and [TestResult.Falsified] otherwise */
public infix fun <T> T.neverEquals(unexpected: T): TestResult =
    if (this != unexpected) TestResult.Satisfied else {
        TestResult.Falsified(Falsification.UnexpectedResult(expected = "NOT $unexpected", actual = toString()))
    }
