package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class SimplifierFilterTest {

    @Test
    fun filterApplyFilterToSequences() {
        val simplifier = object : Simplifier<Int> {

            @ExperimentalKwikFuzzer
            override fun simplify(value: Int): Sequence<Int> =
                sequenceOf(1, 2, 3, 4, 5).map { value - it }
        }

        val list = simplifier
            .filter { it % 2 != 0 }
            .simplify(100)
            .toList()

        assertEquals(listOf(99, 97, 95), list)
    }


    @Test
    fun filterApplyFilterNotToSequences() {
        val simplifier = object : Simplifier<Int> {

            @ExperimentalKwikFuzzer
            override fun simplify(value: Int): Sequence<Int> =
                sequenceOf(1, 2, 3, 4, 5).map { value - it }
        }

        val list = simplifier
            .filterNot { it % 2 == 0 }
            .simplify(100)
            .toList()

        assertEquals(listOf(99, 97, 95), list)
    }
}
