package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MapTest : AbstractGeneratorTest() {
    override val generator: Generator<String> = Generator.create { it.nextInt() }.map { it.toString() }

    @Test
    fun applyTransform() {
        val gen: Generator<Pair<Int, String>> = Generator.create { it.nextInt() }.map { it to it.toString() }

        gen.randoms(12).take(200).forEach { (i, s) ->
            assertEquals(i, s.toInt())
        }
    }

    @Test
    fun produceAsManyDifferentValues() {
        val source = Generator.create { it.nextInt() }
        val transformed: Generator<String> = source.map { it.toString() }

        val sourceResults = source.randoms(37).take(200).toSet()
        val transformedResults = transformed.randoms(37).take(200).toSet()

        assertEquals(sourceResults.size, transformedResults.size)
    }

    @Test
    fun transformSamples() {
        val generator = Generator.create { it.nextInt() }
            .withSamples(42, 77)
            .map { it.toString() }

        assertEquals(setOf("42", "77"), generator.samples)
    }
}
