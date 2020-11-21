package com.github.jcornaz.kwik

public sealed class PropertyResult {
    public object Skip : PropertyResult()
    public object Satisfied : PropertyResult()
    public data class Falsified(val message: String) : PropertyResult()
}

public fun Boolean.alwaysTrue(): PropertyResult = TODO()
public fun Boolean.neverTrue(): PropertyResult = TODO()
public fun Boolean.alwaysFalse(): PropertyResult = TODO()
public fun Boolean.neverFalse(): PropertyResult = TODO()

public infix fun <T> T.alwaysEqualsTo(expected: T): PropertyResult = TODO()
public infix fun <T> T.neverEqualsTo(expected: T): PropertyResult = TODO()
