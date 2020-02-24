package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplificationTree
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random

/**
 * A fuzzer is responsible to both, generate values relevant for testing as well as accompany them by
 * a [SimplificationTree].
 * 
 * @see generate
 */
@ExperimentalKwikFuzzer
interface Fuzzer<out T> {

    /**
     * Returns a [SimplificationTree] with a root item is randomly generated using the given [random].
     *
     * This function can mutate [random]. But it should always return the same value for a given [Random] state.
     * Ex. `generate(Random(0L))` should always return the same value.
     */
    fun generate(random: Random): SimplificationTree<T>
}

/**
 * @deprecated Use [Fuzzer]
 */
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
    override fun generate(random: Random): SimplificationTree<T> =
        simplificationTree(generator.generate(random), simplifier::simplify)
}
