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
     * Returns a sequence of values that are simpler than [value].
     *
     * The result sequence must be finite and should returns values ordered from simpler to more complex.
     *
     * TODO Remove once all implementers have been migrated
     */
    @Deprecated("Use tree instead", ReplaceWith("tree(value).children.map { it.item }"))
    fun simplify(value: T): Sequence<T> =
        tree(value).children.map { it.item }

    /**
     * Returns a [SimplificationTree] with [value] being the root of the tree
     */
    fun tree(value: T): SimplificationTree<T>

    companion object
}
