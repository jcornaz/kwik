package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random

fun <T> Generator.Companion.sequences(
    elementGen: Generator<T>,
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Sequence<T>> = SequenceGenerator(elementGen, minSize, maxSize)

inline fun <reified T> Generator.Companion.sequences(
    minSize: Int = 0,
    maxSize: Int = maxOf(minSize, KWIK_DEFAULT_MAX_SIZE)
): Generator<Sequence<T>> = sequences(Generator.default(), minSize, maxSize)

private class SequenceGenerator<T>(
    private val elementGen: Generator<T>,
    private val minSize: Int,
    private val maxSize: Int
) : Generator<Sequence<T>> {
    override val samples: Set<Sequence<T>> = HashSet<Sequence<T>>().apply {
        if (minSize == 0) add(emptySequence())

        if (minSize <= 1 && maxSize >= 1) {
            elementGen.samples.forEach { add(sequenceOf(it)) }
        }
    }

    init {
        requireValidSizes(minSize, maxSize)
    }

    override fun generate(random: Random): Sequence<T> =
        GeneratedSequence(random.nextLong(), random.nextInt(minSize, maxSize + 1), elementGen)
}

private class GeneratedSequence<T>(
    private val seed: Long,
    private val size: Int,
    private val elementGen: Generator<T>
) : Sequence<T> by sequence<T>({
    val random = Random(seed)

    repeat(size) {
        yield(elementGen.generate(random))
    }
}) {

    override fun toString(): String = joinToString(prefix = "[", postfix = "]", separator = ", ")
}
