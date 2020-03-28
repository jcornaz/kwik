package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer

/**
 * A simplifier is capable of taking a value to return a sequence of values
 * that are in some sense "simpler" than the given value.
 *
 * The definition of what "simpler" means is defined by the implementer of this interface.
 * For instance one may consider `1` simpler than `-189034789235`.
 */
@ExperimentalKwikFuzzer
interface Simplifier<T> {


    /**
     * Returns a sequence of [SimplificationTree] each of them having a root simpler than [value]
     */
    fun simplify(value: T): Sequence<SimplificationTree<T>>

    companion object
}

/**
 * Returns a [SimplificationTree] with [value] being the root of the tree
 */
@ExperimentalKwikFuzzer
internal fun <T> Simplifier<T>.tree(value: T): SimplificationTree<T> =
    SimplificationTree(value, simplify(value))
