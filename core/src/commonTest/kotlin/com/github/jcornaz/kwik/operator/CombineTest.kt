package com.github.jcornaz.kwik.operator

import com.github.jcornaz.kwik.*
import com.github.jcornaz.kwik.generator.doubles
import com.github.jcornaz.kwik.generator.ints
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
            Generator.ints(min = 3).withSamples(1, 2),
            Generator.doubles(min = 3.0).withSamples(1.0, 2.0)
        )


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

    private data class CombinedValues(val x: Int, val y: Double)
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
}

class CombineWithWithTransformTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt() }
            .combineWith(Generator.create { it.nextDouble() }) { x, y -> CombinedValues(x, y) }

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

    private data class CombinedValues(val x: Int, val y: Double)
}
