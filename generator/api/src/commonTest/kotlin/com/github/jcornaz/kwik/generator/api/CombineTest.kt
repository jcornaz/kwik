package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import com.github.jcornaz.kwik.generator.test.shouldBeEquivalentTo
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class CombineWithTransformTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.combine(
            Generator.create { it.nextInt() },
            Generator.create { it.nextDouble() }
        ) { x, y -> CombinedValues(x, y) }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randoms(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() }.withSamples(1, 2),
            Generator.create { it.nextDouble() }.withSamples(3.0, 4.0)
        ) { a, b -> CombinedValues(a, b) }

        assertTrue(gen.randoms(0).take(200).count { (a, b) -> a != b.toInt() } > 150)
    }

    @Test
    fun combineSamples() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() }.withSamples(1, 2),
            Generator.create { it.nextDouble() }.withSamples(3.0, 4.0)
        ) { a, b -> CombinedValues(a, b) }

        assertEquals(
            setOf(
                CombinedValues(1, 3.0),
                CombinedValues(1, 4.0),
                CombinedValues(2, 3.0),
                CombinedValues(2, 4.0)
            ), gen.samples
        )
    }

    @Test
    fun randomValuesContainsSamples() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() + 3 }.withSamples(1, 2),
            Generator.create { it.nextDouble() + 3.0 }.withSamples(1.0, 2.0)
        ) { a, b -> CombinedValues(a, b) }

        assertTrue(gen.randoms(0).take(100).any { (x, y) -> x < 3 && y >= 3 })
        assertTrue(gen.randoms(0).take(100).any { (x, y) -> y < 3 && x >= 3 })
    }

    @Test
    fun ifNoneOfTheSourceCanBeShrinkedThenCombinationCannotBeShrinkedEither() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() },
            Generator.create { it.nextDouble() }
        ) { a, b -> CombinedValues(a, b) }

        gen.shrink(CombinedValues(10, 8.0)).isEmpty()
    }
}

class CombineTest : AbstractGeneratorTest() {

    override val generator: Generator<*> = Generator.combine(
        Generator.create { it.nextInt() },
        Generator.create { it.nextDouble() }
    )

    @Test
    fun isEquivalentToCombineWithTransform() {
        repeat(100) {
            val genA = Random.nextIntGenerator()
            val genB = Random.nextDoubleGenerator()

            Generator.combine(genA, genB)
                .shouldBeEquivalentTo(Generator.combine(genA, genB) { a, b -> a to b })
        }
    }
}


class CombineWithTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt() }.combineWith(Generator.create { it.nextDouble() })

    @Test
    fun isEquivalentToCombine() {
        repeat(100) {
            val genA = Random.nextIntGenerator()
            val genB = Random.nextDoubleGenerator()

            genA.combineWith(genB)
                .shouldBeEquivalentTo(Generator.combine(genA, genB))
        }
    }
}

class CombineWithWithTransformTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt() }
            .combineWith(Generator.create { it.nextDouble() }) { x, y ->
                CombinedValues(
                    x,
                    y
                )
            }

    @Test
    fun isEquivalentToCombineWithTransform() {
        repeat(100) {
            val genA = Random.nextIntGenerator()
            val genB = Random.nextDoubleGenerator()

            genA.combineWith(genB) { a, b -> CombinedValues(a, b) }
                .shouldBeEquivalentTo(Generator.combine(genA, genB) { a, b -> CombinedValues(a, b) })
        }
    }
}

private data class CombinedValues(val x: Int, val y: Double)

private fun Random.nextIntGenerator(): Generator<Int> {
    val samples = List(nextInt(5)) { nextInt() }.toSet()

    val shrinker: (Int) -> Collection<Int> = if (nextBoolean())
        { _ -> emptyList() }
    else
        { value ->
            when {
                value == 0 -> emptyList()
                value == -1 || value == 1 -> listOf(0)
                value < 0 -> listOf(value / 2, value + 1)
                else -> listOf(value / 2, value - 1)
            }
        }


    return object : Generator<Int> {
        override val samples: Set<Int>
            get() = samples

        override fun randoms(seed: Long): Sequence<Int> = randomSequence(seed) { it.nextInt() }
        override fun shrink(value: Int): Collection<Int> = shrinker(value)
    }
}

private fun Random.nextDoubleGenerator(): Generator<Double> {
    val samples = List(nextInt(5)) { nextDouble() }.toSet()

    val shrinker: (Double) -> Collection<Double> = if (nextBoolean())
        { _ -> emptyList() }
    else
        { value ->
            when {
                value == 0.0 -> emptyList()
                value < 0 -> listOf(value / 2.0)
                else -> listOf(value / 2.0)
            }
        }


    return object : Generator<Double> {
        override val samples: Set<Double>
            get() = samples

        override fun randoms(seed: Long): Sequence<Double> = randomSequence(seed) { it.nextDouble() }
        override fun shrink(value: Double): Collection<Double> = shrinker(value)
    }
}
