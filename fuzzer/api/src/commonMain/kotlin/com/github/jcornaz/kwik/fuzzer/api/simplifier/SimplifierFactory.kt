package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer


/**
 * Returns a [Simplifier] that calls [simplify] in order to get simpler value.
 *
 * @param simplify A function that return a sequence of values *simpler* than its input.
 *                 The elements in the sequence must be ordered from simplest to most complex
 */
@ExperimentalKwikFuzzer
fun <T> simplifier(simplify: (T) -> Sequence<T>): Simplifier<T> = object : Simplifier<T> {
    override fun simplify(value: T): Sequence<SimplificationTree<T>> =
        simplify(value).map { simplificationTree(it, simplify) }
}

/**
 * Create a [Simplifier] that can simplify pairs.
 *
 * @param first Simplifier for the first elements of the pairs
 * @param second Simplifier for the second elements of the pairs
 */
@ExperimentalKwikFuzzer
fun <A, B> Simplifier.Companion.pair(
    first: Simplifier<A>,
    second: Simplifier<B>
): Simplifier<Pair<A, B>> =
    simplifier { (firstValue, secondValue) ->
        sequence {
            val i1 = first.simplify(firstValue).map { it.item }.map { it to secondValue }.iterator()
            val i2 = second.simplify(secondValue).map { it.item }.map { firstValue to it }.iterator()

            while (i1.hasNext() || i2.hasNext()) {
                if (i1.hasNext()) yield(i1.next())
                if (i2.hasNext()) yield(i2.next())
            }
        }
    }

/**
 * Create a [Simplifier] that can simplify triples.
 *
 * @param first Simplifier for the first elements of the pairs
 * @param second Simplifier for the second elements of the pairs
 * @param third Simplifier for the third elements of the pairs
 */
@ExperimentalKwikFuzzer
fun <A, B, C> Simplifier.Companion.triple(
    first: Simplifier<A>,
    second: Simplifier<B>,
    third: Simplifier<C>
): Simplifier<Triple<A, B, C>> =
    simplifier { (firstValue, secondValue, thirdValue) ->
        sequence {
            val i1 = first.simplify(firstValue)
                .map { it.item }
                .map { Triple(it, secondValue, thirdValue) }
                .iterator()

            val i2 = second.simplify(secondValue)
                .map { it.item }
                .map { Triple(firstValue, it, thirdValue) }
                .iterator()

            val i3 = third.simplify(thirdValue)
                .map { it.item }
                .map { Triple(firstValue, secondValue, it) }
                .iterator()

            while (i1.hasNext() || i2.hasNext() || i3.hasNext()) {
                if (i1.hasNext()) yield(i1.next())
                if (i2.hasNext()) yield(i2.next())
                if (i3.hasNext()) yield(i3.next())
            }
        }
    }
