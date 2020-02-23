package com.github.jcornaz.kwik.generator.api.simplification

import com.github.jcornaz.kwik.generator.api.ExperimentalKwikGeneratorApi
import kotlin.test.assertEquals

@ExperimentalKwikGeneratorApi
fun <T> assertTreeEquals(expected: SimplificationTree<T>, actual: SimplificationTree<T>) {
    assertEquals(expected.root, actual.root)
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
