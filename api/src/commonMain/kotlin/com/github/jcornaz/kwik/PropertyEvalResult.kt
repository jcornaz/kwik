package com.github.jcornaz.kwik

public sealed class PropertyEvalResult {
    public object Skip : PropertyEvalResult()
    public object Satisfied : PropertyEvalResult()
    public data class Falsified(
        val shortMessage: String? = null,
        val expected: String,
        val actual: String,
    ) : PropertyEvalResult()
}

public fun Boolean.alwaysTrue(): PropertyEvalResult =
    if (this) PropertyEvalResult.Satisfied else PropertyEvalResult.Falsified(expected = "true", actual = "false")

public fun Boolean.neverTrue(): PropertyEvalResult =
    if (this) PropertyEvalResult.Falsified(expected = "false", actual = "true") else PropertyEvalResult.Satisfied

public fun Boolean.alwaysFalse(): PropertyEvalResult =
    if (this) PropertyEvalResult.Falsified(expected = "false", actual = "true") else PropertyEvalResult.Satisfied

public fun Boolean.neverFalse(): PropertyEvalResult =
    if (this) PropertyEvalResult.Satisfied else PropertyEvalResult.Falsified(expected = "true", actual = "false")

public infix fun <T> T.alwaysEquals(expected: T): PropertyEvalResult = TODO()
public infix fun <T> T.neverEquals(expected: T): PropertyEvalResult = TODO()
