package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.tree.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.tree.simplificationTree
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence


/**
 * Definition of how to create values of type [T] that are relevant for testing.
 *
 *
 * This is the combination of
 * * a [Generator] responsible for value generation,
 * * a [Simplifier] responsbile for finding smaller/simpler values,
 * * some [guarantees] that can force the property evaluation to run as long as necessary
 *   to make sure that all guarantees are fulfilled
 */
@ExperimentalKwikFuzzer
interface Fuzzer<out T> {
    fun fuzz(seed: Long, count: Int): Sequence<SimplificationTree<T>>
}

@Deprecated("Use fuzzer instead")
@ExperimentalKwikFuzzer
data class OldFuzzer<T>(
    val generator: Generator<T>,
    val simplifier: Simplifier<T>,
    val guarantees: List<(T) -> Boolean> = emptyList()
)

/**
 * Returns a [OldFuzzer] backed by this [Generator].
 *
 * You may pass a [simplifier]. Otherwise the resulting fuzzer will not support input simplification.
 */
@ExperimentalKwikFuzzer
@Deprecated("Use toFuzzer instead")
fun <T> Generator<T>.toOldFuzzer(simplifier: Simplifier<T>): OldFuzzer<T> =
    OldFuzzer(this, simplifier)

/**
 * Returns a [Fuzzer] backed by this [Generator] the given [simplifier]
 */
@ExperimentalKwikFuzzer
fun <T> Generator<T>.toFuzzer(simplifier: Simplifier<T>): Fuzzer<T> =
    SimpleFuzzer(this, simplifier)

@ExperimentalKwikFuzzer
private class SimpleFuzzer<T>(
    val generator: Generator<T>,
    val simplifier: Simplifier<T>
) : Fuzzer<T> {
    override fun fuzz(seed: Long, count: Int): Sequence<SimplificationTree<T>> =
        generator.randomSequence(seed).take(count).map { simplificationTree(it, simplifier::simplify) }
}
