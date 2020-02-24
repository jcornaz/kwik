package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
fun <T> assertTreeEquals(expected: SimplificationTree<T>, actual: SimplificationTree<T>) {
    assertEquals(expected.item, actual.item)
    val actualBranches = actual.children.toList()
    val expectedBranches = expected.children.toList()

    assertEquals(expectedBranches.size, actualBranches.size)
    expectedBranches.forEachIndexed { index, expectedTree ->
        assertTreeEquals(
            expectedTree,
            actualBranches[index]
        )
    }
}
