package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("DEPRECATION")
class CombineTest : AbstractGeneratorTest() {

    override val generator: Generator<*> = Generator.combine(
        { it.nextInt() },
        { it.nextDouble() }
    )

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.combine(
            { it.nextInt() },
            { it.nextInt() }
        )

        assertTrue(gen.randomSequence(123).take(200).count { (a, b) -> a != b } > 150)
    }
}

class PairTest : AbstractGeneratorTest() {

    override val generator: Generator<*> = Generator.pair(
        { it.nextInt() },
        { it.nextDouble() }
    )

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.pair(
            { it.nextInt() },
            { it.nextInt() }
        )

        assertTrue(gen.randomSequence(123).take(200).count { (a, b) -> a != b } > 150)
    }

    @Test
    fun canUseSameGeneratorForBothElements() {
        val itemGen = Generator { it.nextInt() }

        assertEquals(
            Generator.pair(itemGen).randomSequence(0).take(100).toList(),
            Generator.pair(itemGen, itemGen).randomSequence(0).take(100).toList()
        )
    }
}

class CombineWithTransformTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.combine(
            { it.nextInt() },
            { it.nextDouble() }
        ) { x, y -> CombinedValues(x, y) }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.combine(
            Generator { it.nextInt() }.withSamples(1, 2),
            Generator { it.nextDouble() }.withSamples(3.0, 4.0)
        ) { a, b -> CombinedValues(a, b) }

        assertTrue(gen.randomSequence(0).take(200).count { (a, b) -> a != b.toInt() } > 150)
    }

    private data class CombinedValues(val x: Int, val y: Double)
}

class CombineWithTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator { it.nextInt() }.combineWith { it.nextDouble() }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator { it.nextInt() }.combineWith { it.nextInt() }

        assertTrue(gen.randomSequence(123).take(200).count { (a, b) -> a != b } > 150)
    }
}

class CombineWithWithTransformTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator { it.nextInt() }
            .combineWith({ it.nextDouble() }) { x, y ->
                CombinedValues(
                    x,
                    y
                )
            }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator { it.nextInt() }
            .combineWith({ it.nextInt() }) { a, b -> a to b }

        assertTrue(gen.randomSequence(123).take(200).count { (a, b) -> a != b } > 150)
    }

    private data class CombinedValues(val x: Int, val y: Double)
}
