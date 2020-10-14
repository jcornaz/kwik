package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.ExperimentalKwikApi
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikApi
class SimplifierFilterTest {

    @Test
    fun filterApplyFilterToSequences() {
        val simplifier = Simplifier<Int> { value -> sequenceOf(1, 2, 3, 4, 5).map { value - it } }

        val list = simplifier
            .filter { it % 2 != 0 }
            .simplify(100)
            .toList()

        assertEquals(listOf(99, 97, 95), list)
    }


    @Test
    fun filterApplyFilterNotToSequences() {
        val simplifier = Simplifier<Int> { value -> sequenceOf(1, 2, 3, 4, 5).map { value - it } }

        val list = simplifier
            .filterNot { it % 2 == 0 }
            .simplify(100)
            .toList()

        assertEquals(listOf(99, 97, 95), list)
    }
}
