package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class SimplifierFilterTest {

    @Test
    fun keepsOnlyTreesWithRootMatchingThePredicate() {
        val simplifier = simplifier<Int> { value ->
            sequenceOf(1, 2, 3, 4, 5).map { value - it }
        }

        val list = simplifier
            .filter { it % 2 != 0 }
            .simplify(100)
            .map { it.item }
            .toList()

        assertEquals(listOf(99, 97, 95), list)
    }

    @Test
    fun keepsOnlyTreesWithChildrenMatchingThePredicate() {
        val simplifier = simplifier<Int> { value ->
            sequenceOf(1, 2, 3).map { value - it }
        }

        val list = simplifier
            .filter { it % 2 != 0 }
            .simplify(100)
            .flatMap { tree -> tree.children.map { it.item } }
            .toList()

        assertEquals(listOf(97, 95), list)
    }
}

@ExperimentalKwikFuzzer
class SimplifierFilterNotTest {
    @Test
    fun keepsOnlyTreesWithRootNotMatchingThePredicate() {
        val simplifier = simplifier<Int> { value ->
            sequenceOf(1, 2, 3, 4, 5).map { value - it }
        }

        val list = simplifier
            .filterNot { it % 2 == 0 }
            .simplify(100)
            .map { it.item }
            .toList()

        assertEquals(listOf(99, 97, 95), list)
    }

    @Test
    fun keepsOnlyTreesWithChildrenNotMatchingThePredicate() {
        val simplifier = simplifier<Int> { value ->
            sequenceOf(1, 2, 3).map { value - it }
        }

        val list = simplifier
            .filterNot { it % 2 == 0 }
            .simplify(100)
            .flatMap { tree -> tree.children.map { it.item } }
            .toList()

        assertEquals(listOf(97, 95), list)
    }
}
