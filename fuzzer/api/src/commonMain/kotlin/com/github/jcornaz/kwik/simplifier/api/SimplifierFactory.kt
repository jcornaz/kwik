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
    sequence {
        val firstIterator = first.simplify(firstValue).iterator()
        val secondIterator = second.simplify(secondValue).iterator()

        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            yield(firstIterator.next() to secondValue)
            yield(firstValue to secondIterator.next())
        }

        firstIterator.forEach {
            yield(it to secondValue)
        }

        secondIterator.forEach {
            yield(firstValue to it)
        }
    }
}
