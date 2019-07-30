package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CombineTest : AbstractGeneratorTest() {

    override val generator: Generator<*> = Generator.combine(
        Generator.create { it.nextInt() },
        Generator.create { it.nextDouble() }
    )

    @Test
    fun combineTheValues() {
        assertTrue(generator.randoms(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() },
            Generator.create { it.nextInt() }
        )

        assertTrue(gen.randoms(123).take(200).count { (a, b) -> a != b } > 150)
    }

    @Test
    fun combineSamples() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() }.withSamples(1, 2),
            Generator.create { it.nextInt().toString() }.withSamples("one", "two")
        )

        assertEquals(setOf(1 to "one", 1 to "two", 2 to "one", 2 to "two"), gen.samples)
    }

    @Test
    fun randomValuesContainsSamples() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() + 3 }.withSamples(1, 2),
            Generator.create { it.nextDouble() + 3.0 }.withSamples(1.0, 2.0)
        )

        assertTrue(gen.randoms(0).take(100).any { (x, y) -> x < 3 && y >= 3 })
        assertTrue(gen.randoms(0).take(100).any { (x, y) -> y < 3 && x >= 3 })
    }

    @Test
    fun ifNoneOfTheSourceCanBeShrinkedThenCombinationCannotBeShrinkedEither() {
        val gen = Generator.combine(
            Generator.create { it.nextInt() },
            Generator.create { it.nextDouble() }
        )

        gen.shrink(10 to 8.0).isEmpty()
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

class CombineWithTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt() }.combineWith(Generator.create { it.nextDouble() })

    @Test
    fun combineTheValues() {
        assertTrue(generator.randoms(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.create { it.nextInt() }.combineWith(Generator.create { it.nextInt() })

        assertTrue(gen.randoms(123).take(200).count { (a, b) -> a != b } > 150)
    }

    @Test
    fun combineSamples() {
        val gen = Generator.create { it.nextInt() }.withSamples(1, 2)
            .combineWith(Generator.create { it.nextInt().toString() }.withSamples("one", "two"))

        assertEquals(setOf(1 to "one", 1 to "two", 2 to "one", 2 to "two"), gen.samples)
    }

    @Test
    fun randomValuesContainsSamples() {
        val gen = Generator.create { it.nextInt() + 3 }.withSamples(1, 2)
            .combineWith(Generator.create { it.nextInt().toString() }.withSamples("one", "two"))

        assertTrue(gen.randoms(0).take(100).any { (x, y) -> x < 3 && y !in setOf("one", "two") })
        assertTrue(gen.randoms(0).take(100).any { (x, y) -> y in setOf("one", "two") && x >= 3 })
    }


    @Test
    fun ifNoneOfTheSourceCanBeShrinkedThenCombinationCannotBeShrinkedEither() {
        val gen = Generator.create { it.nextInt() }
            .combineWith(Generator.create { it.nextDouble() })

        gen.shrink(10 to 8.0).isEmpty()
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
        assertTrue(generator.randoms(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.create { it.nextInt() }
            .combineWith(Generator.create { it.nextInt() }) { a, b -> a to b }

        assertTrue(gen.randoms(123).take(200).count { (a, b) -> a != b } > 150)
    }

    @Test
    fun combineSamples() {
        val gen = Generator.create { it.nextInt() }.withSamples(1, 2)
            .combineWith(Generator.create { it.nextDouble() }.withSamples(3.0, 4.0)) { a, b ->
                CombinedValues(a, b)
            }

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
        val gen = Generator.create { it.nextInt() + 3 }.withSamples(1, 2)
            .combineWith(Generator.create { it.nextDouble() + 3.0 }.withSamples(-1.0, -2.0)) { a, b ->
                CombinedValues(a, b)
            }

        assertTrue(gen.randoms(0).take(100).any { (x, y) -> x < 3 && y >= 3 })
        assertTrue(gen.randoms(0).take(100).any { (x, y) -> y < 3 && x >= 3 })
    }

    @Test
    fun ifNoneOfTheSourceCanBeShrinkedThenCombinationCannotBeShrinkedEither() {
        val gen = Generator.create { it.nextInt() }
            .combineWith(Generator.create { it.nextDouble() }) { a, b ->
                CombinedValues(a, b)
            }

        gen.shrink(CombinedValues(10, 8.0)).isEmpty()
    }
}

private data class CombinedValues(val x: Int, val y: Double)
