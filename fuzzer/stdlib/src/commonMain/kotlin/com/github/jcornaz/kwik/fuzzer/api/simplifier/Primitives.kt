package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.math.absoluteValue

/**
 * Returns a simplifier capable of simplifying [Int].
 *
 * zero is the simplest value.
 * the closest to zero, the simpler.
 * positive values are also considered simpler than their negative counterpart.
 */
@ExperimentalKwikFuzzer
fun Simplifier.Companion.int(): Simplifier<Int> = simplifier { value ->
    when (value) {
        0 -> emptySequence()
        -1, 1 -> sequenceOf(0)
        else -> sequence {
            yield(value / 2)

            if (value < 0) yield(value.absoluteValue)

            yield(if (value < 0) value + 1 else value - 1)
        }
    }
}
