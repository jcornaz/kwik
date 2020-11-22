package com.github.jcornaz.kwik

public sealed class PropertyEvalResult {
    public object Skip : PropertyEvalResult()
    public object Satisfied : PropertyEvalResult()
    public data class Falsified(val expected: String, val actual: String) : PropertyEvalResult()
}

public fun Boolean.alwaysTrue(): PropertyEvalResult = this alwaysEquals true
public fun Boolean.neverTrue(): PropertyEvalResult = this alwaysEquals false
public fun Boolean.alwaysFalse(): PropertyEvalResult = this alwaysEquals false
public fun Boolean.neverFalse(): PropertyEvalResult = this alwaysEquals true

public infix fun <T> T.alwaysEquals(expected: T): PropertyEvalResult =
    if (this == expected) PropertyEvalResult.Satisfied
    else PropertyEvalResult.Falsified(expected = expected.toString(), actual = toString())

public infix fun <T> T.neverEquals(unexpected: T): PropertyEvalResult =
    if (this == unexpected) PropertyEvalResult.Falsified(expected = "NOT $unexpected", actual = toString())
    else PropertyEvalResult.Satisfied
