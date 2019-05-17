package com.github.jcornaz.kwik

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 * using the provided [transform] function applied to each pair of elements.
 */
fun <A, B, R> Generator<A>.zip(
    other: Generator<B>,
    transform: (A, B) -> R
): Generator<R> = MergingGenerator(this, other, transform)

/**
 * Returns a generator of values built from the elements of `this` generator and the [other] generator
 */
infix fun <A, B> Generator<A>.zip(
    other: Generator<B>
): Generator<Pair<A, B>> = MergingGenerator(this, other) { a, b -> a to b }

private class MergingGenerator<A, B, R>(
    private val generator1: Generator<A>,
    private val generator2: Generator<B>,
    private val transform: (A, B) -> R
) : Generator<R> {
    override fun randoms(seed: Long): Sequence<R> =
        generator1.randoms(seed).zip(generator2.randoms(seed), transform)
}
