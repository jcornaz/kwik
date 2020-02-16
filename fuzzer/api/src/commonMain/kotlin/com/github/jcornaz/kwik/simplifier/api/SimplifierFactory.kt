package com.github.jcornaz.kwik.simplifier.api

@ExperimentalKwikFuzzer
internal fun <T> simplifier(simplify: (T) -> Sequence<T>): Simplifier<T> = object : Simplifier<T> {
    override fun simplify(value: T): Sequence<T> = simplify(value)
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
): Simplifier<Pair<A, B>> = simplifier { (firstValue, secondValue) ->
    first.simplify(firstValue).map { it to secondValue }
        .plus(second.simplify(secondValue).map { firstValue to it })
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
): Simplifier<Triple<A, B, C>> = simplifier { (firstValue, secondValue, thirdValue) ->
    first.simplify(firstValue).map { Triple(it, secondValue, thirdValue) }
        .plus(second.simplify(secondValue).map { Triple(firstValue, it, thirdValue) })
        .plus(third.simplify(thirdValue).map { Triple(firstValue, secondValue, it) })
}
