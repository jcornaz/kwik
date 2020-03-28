package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.Simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.tree
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
        simplifier.tree(generator.generate(random))
}
