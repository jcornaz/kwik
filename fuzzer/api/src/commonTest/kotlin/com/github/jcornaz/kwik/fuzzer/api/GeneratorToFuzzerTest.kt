package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplifier
import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.tree.assertTreeEquals
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplificationTreeOf
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class GeneratorToFuzzerTest {

    @Test
    fun generateRootsAccordingToGenerator() {
        val generator = Generator.create { it.nextInt() }
        val fuzzer = generator.toFuzzer(dontSimplify())

        repeat(100) {
            val seed = Random.nextLong()

            assertEquals(
                generator.generate(Random(seed)),
                fuzzer.generate(Random(seed)).item
            )
        }
    }

    @Test
    fun generateTreesAccordingToSimplifier() {
        val generator = Generator.create { 12 }
        val simplifier = simplifier<Int> {
            if (it == 12) sequenceOf(1, 2, 3, 4) else emptySequence()
        }

        val fuzzer = generator.toFuzzer(simplifier)

        assertTreeEquals(
            SimplificationTree(
                12,
                sequenceOf(
                    1,
                    2,
                    3,
                    4
                ).map { simplificationTreeOf(it) }),
            fuzzer.generate(Random)
        )
    }
}
