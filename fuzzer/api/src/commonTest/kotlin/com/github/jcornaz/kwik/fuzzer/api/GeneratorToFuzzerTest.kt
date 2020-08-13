package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class GeneratorToFuzzerTest {

    @Test
    fun generatorToFuzzzerCreateAGeneratorWithThatFuzzer() {
        val generator = Generator { it: Random -> it.nextInt() }
        val fuzzer = generator.toFuzzer(dontSimplify())
        assertSame(generator, fuzzer.generator)
    }

    @Test
    fun generatorToFuzzerAcceptsASimplifier() {
        val generator = Generator { it: Random -> it.nextInt() }
        val simplifier = dontSimplify<Int>()

        val fuzzer = generator.toFuzzer(simplifier)
        assertSame(fuzzer.simplifier, simplifier)
    }

    @Test
    fun hasNoGuaranteesByDefault() {
        assertTrue(
            Generator { it: Random -> it.nextInt() }
                .toFuzzer(dontSimplify())
                .guarantees
                .isEmpty()
        )
    }
}
