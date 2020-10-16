package com.github.jcornaz.kwik.fuzzer.stdlib.simplifier

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import kotlin.math.absoluteValue

/**
 * Returns a simplifier capable of simplifying [Int].
 *
 * zero is the simplest value.
 * the closest to zero, the simpler.
 * positive values are also considered simpler than their negative counterpart.
 */
@ExperimentalKwikApi
val Simplifier.Companion.int: Simplifier<Int>
    get() = IntSimplifier

@ExperimentalKwikApi
private object IntSimplifier : Simplifier<Int> {
    override fun simplify(value: Int): Sequence<Int> = when (value) {
        0 -> emptySequence()
        1 -> sequenceOf(0)
        -1 -> sequenceOf(0, 1)
        else -> sequence {
            yield(value / 2)

            if (value < 0) yield(value.absoluteValue)

            if (value.absoluteValue > 2)
                yield(if (value < 0) value + 1 else value - 1)
        }
    }
}
