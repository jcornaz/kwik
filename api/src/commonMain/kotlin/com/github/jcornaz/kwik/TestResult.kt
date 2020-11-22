package com.github.jcornaz.kwik

public sealed class TestResult {
    public object Skip : TestResult()
    public object Satisfied : TestResult()
    public data class Falsified(val expected: String, val actual: String) : TestResult()
    public data class Error(val cause: Throwable): TestResult()
}

public fun Boolean.alwaysTrue(): TestResult = this alwaysEquals true
public fun Boolean.neverTrue(): TestResult = this alwaysEquals false
public fun Boolean.alwaysFalse(): TestResult = this alwaysEquals false
public fun Boolean.neverFalse(): TestResult = this alwaysEquals true

public infix fun <T> T.alwaysEquals(expected: T): TestResult =
    if (this == expected) TestResult.Satisfied
    else TestResult.Falsified(expected = expected.toString(), actual = toString())

public infix fun <T> T.neverEquals(unexpected: T): TestResult =
    if (this == unexpected) TestResult.Falsified(expected = "NOT $unexpected", actual = toString())
    else TestResult.Satisfied
