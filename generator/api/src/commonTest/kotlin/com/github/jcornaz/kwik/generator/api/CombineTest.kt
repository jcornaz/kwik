package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertTrue

class CombineTest : AbstractGeneratorTest() {

    override val generator: Generator<*> = Generator.combine(
        Generator.create { it.nextInt() },
        Generator.create { it.nextDouble() }
    )

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() },
            Generator.create { it.nextInt() }
        )

        assertTrue(gen.randomSequence(123).take(200).count { (a, b) -> a != b } > 150)
    }
}

class CombineWithTransformTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.combine(
            Generator.create { it.nextInt() },
            Generator.create { it.nextDouble() }
        ) { x, y -> CombinedValues(x, y) }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() }.withSamples(1, 2),
            Generator.create { it.nextDouble() }.withSamples(3.0, 4.0)
        ) { a, b -> CombinedValues(a, b) }

        assertTrue(gen.randomSequence(0).take(200).count { (a, b) -> a != b.toInt() } > 150)
    }

    private data class CombinedValues(val x: Int, val y: Double)
}

class CombineWithTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt() }.combineWith(Generator.create { it.nextDouble() })

    @Test
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.create { it.nextInt() }.combineWith(Generator.create { it.nextInt() })

        assertTrue(gen.randomSequence(123).take(200).count { (a, b) -> a != b } > 150)
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
    fun combineTheValues() {
        assertTrue(generator.randomSequence(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.create { it.nextInt() }
            .combineWith(Generator.create { it.nextInt() }) { a, b -> a to b }

        assertTrue(gen.randomSequence(123).take(200).count { (a, b) -> a != b } > 150)
    }

    private data class CombinedValues(val x: Int, val y: Double)
}
