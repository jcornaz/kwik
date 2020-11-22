package com.github.jcornaz.kwik

public sealed class TestResult {
    public object Skip : TestResult()
    public object Satisfied : TestResult()
    public data class Falsified(val falsification: Falsification) : TestResult()
}

public sealed class Falsification {
    public data class Error(val throwable: Throwable) : Falsification()
    public data class UnexpectedResult(val expected: String, val actual: String) : Falsification()
}

public fun Boolean.alwaysTrue(): TestResult = this alwaysEquals true
public fun Boolean.neverTrue(): TestResult = this alwaysEquals false
public fun Boolean.alwaysFalse(): TestResult = this alwaysEquals false
public fun Boolean.neverFalse(): TestResult = this alwaysEquals true

public infix fun <T> T.alwaysEquals(expected: T): TestResult =
    if (this == expected) TestResult.Satisfied
    else TestResult.Falsified(Falsification.UnexpectedResult(expected = expected.toString(), actual = toString()))

public infix fun <T> T.neverEquals(unexpected: T): TestResult =
    if (this == unexpected) TestResult.Falsified(Falsification.UnexpectedResult(expected = "NOT $unexpected", actual = toString()))
    else TestResult.Satisfied
