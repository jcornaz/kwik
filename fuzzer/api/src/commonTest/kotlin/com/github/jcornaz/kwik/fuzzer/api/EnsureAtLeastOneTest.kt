package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikApi
class EnsureAtLeastOneTest {

    @Test
    fun addsToGuaranteeList() {
        val predicate: (Int) -> Boolean = { true }

        val fuzzer = Generator { it: Random -> it.nextInt() }.toFuzzer(dontSimplify()).ensureAtLeastOne(predicate)

        assertEquals(listOf(predicate), fuzzer.guarantees)
    }
}
