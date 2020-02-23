package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikGeneratorApi
class WithSimplifierTest {

    @Test
    fun returnsGeneratorThatUseSimplifierToBuildTree() {
        val branches = Generator.create { it.nextInt() }
            .withSimplification {
                sequenceOf(1, 2, 3)
            }
            .generateSampleTree(Random)
            .children
            .map { it.root }
            .toList()

        assertEquals(listOf(1, 2, 3), branches)
    }

    @Test
    fun keepsRootOfSourceGenerator() {
        val root = Generator.create { 42 }
            .generateSampleTree(Random)
            .root

        assertEquals(42, root)
    }
}
