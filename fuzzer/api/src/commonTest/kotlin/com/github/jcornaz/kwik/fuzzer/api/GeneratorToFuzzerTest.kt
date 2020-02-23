package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.tree.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.tree.assertTreeEquals
import com.github.jcornaz.kwik.fuzzer.api.simplifier.tree.simplestValue
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

@ExperimentalKwikFuzzer
class GeneratorToFuzzerTest {

    @Test
    fun generateRootsAccordingToGenerator() {
        val generator = Generator.create { it.nextInt() }
        val fuzzer = generator.toFuzzer(dontSimplify())

        val fromGenerator = generator.randomSequence(seed = 12).take(100).toList()
        val fromFuzzer = fuzzer.fuzz(seed = 12, count = 100).map { it.root }.toList()
        assertEquals(fromGenerator, fromFuzzer)
    }

    @Test
    fun generateTreesAccordingToSimplifier() {
        val generator = Generator.create { 12 }
        val simplifier = simplifier<Int> {
            if (it == 12) sequenceOf(1, 2, 3, 4) else emptySequence()
        }

        val fuzzer = generator.toFuzzer(simplifier)

        assertTreeEquals(
            SimplificationTree(12, sequenceOf(1, 2, 3, 4).map { simplestValue(it) }),
            fuzzer.fuzz(0, 1).single()
        )
    }
}
