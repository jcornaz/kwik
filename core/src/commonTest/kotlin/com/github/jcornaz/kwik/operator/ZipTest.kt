package com.github.jcornaz.kwik.operator

import com.github.jcornaz.kwik.AbstractGeneratorTest
import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.zip
import kotlin.test.Test
import kotlin.test.assertTrue

class ZipTest : AbstractGeneratorTest() {

    override val generator: Generator<*> = Generator.create { it.nextInt() } zip Generator.create { it.nextDouble() }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randoms(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.create { it.nextInt() } zip Generator.create { it.nextInt() }

        assertTrue(gen.randoms(123).take(200).count { (a, b) -> a != b } > 150)
    }
}

class ZipWithTransformTest : AbstractGeneratorTest() {

    override val generator: Generator<*> =
        Generator.create { it.nextInt() }
            .zip(Generator.create { it.nextDouble() }) { x, y -> CombinedValues(x, y) }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randoms(0).take(200).distinct().count() > 190)
    }

    @Test
    fun combineDifferentValues() {
        val gen = Generator.create { it.nextInt() }
            .zip(Generator.create { it.nextInt() }) { a, b -> a to b }

        assertTrue(gen.randoms(123).take(200).count { (a, b) -> a != b } > 150)
    }

    private data class CombinedValues(val x: Int, val y: Double)
}
