package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class EnsureAtLeastOneTest {

    @Test
    fun addsToGuaranteeList() {
        val predicate: (Int) -> Boolean = { true }

        val fuzzer = Generator.create { it.nextInt() }.toFuzzer(dontSimplify()).ensureAtLeastOne(predicate)

        assertEquals(listOf(predicate), fuzzer.guarantees)
    }
}
