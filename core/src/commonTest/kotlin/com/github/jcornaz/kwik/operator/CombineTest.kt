package com.github.jcornaz.kwik.operator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.combine
import com.github.jcornaz.kwik.combineWith
import kotlin.test.Test
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
            Generator.create { it.nextInt() },
            Generator.create { it.nextInt() }
        ) { a, b -> a to b }

        assertTrue(gen.randoms(123).take(200).count { (a, b) -> a != b } > 150)
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
}

class ZipWithTransformTest : AbstractGeneratorTest() {

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

    private data class CombinedValues(val x: Int, val y: Double)
}
