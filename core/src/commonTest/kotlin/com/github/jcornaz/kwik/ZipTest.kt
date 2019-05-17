package com.github.jcornaz.kwik

import kotlin.test.Test
import kotlin.test.assertTrue

class ZipTest : AbstractGeneratorTest() {

    override val generator: Generator<*> = Generator.create { it.nextInt() } zip Generator.create { it.nextDouble() }

    @Test
    fun combineTheValues() {
        assertTrue(generator.randoms(0).take(200).distinct().count() > 190)
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

    private data class CombinedValues(val x: Int, val y: Double)
}
