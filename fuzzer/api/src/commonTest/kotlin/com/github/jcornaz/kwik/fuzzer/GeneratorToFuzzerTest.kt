package com.github.jcornaz.kwik.fuzzer

import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

@ExperimentalKwikFuzzer
class GeneratorToFuzzerTest {

    @Test
    fun generatorToFuzzzerCreateAGeneratorWithThatFuzzer() {
        val generator = Generator.create { it.nextInt() }
        val fuzzer = generator.toFuzzer()
        assertSame(generator, fuzzer.generator)
    }

    @Test
    fun generatorToFuzzerAcceptsAShrinker() {
        val generator = Generator.create { it.nextInt() }
        val shrinker = object : Shrinker<Int> {
            @ExperimentalKwikFuzzer
            override fun shrink(value: Int): Sequence<Int> = throw UnsupportedOperationException()
        }

        val fuzzer = generator.toFuzzer(shrinker)
        assertSame(fuzzer.shrinker, shrinker)
    }

    @Test
    fun hasNoGuaranteesByDefault() {
        assertTrue(
            Generator.create { it.nextInt() }
                .toFuzzer()
                .guarantees
                .isEmpty()
        )
    }
}
