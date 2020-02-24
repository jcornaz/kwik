package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplificationTree
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class SimplificationTreeTest {

    @Test
    fun rootIsGivenValue() {
        repeat(100) {
            val value = Random.nextInt()
            assertEquals(value, simplificationTree(
                value
            ) { emptySequence() }.item)
        }
    }

    @Test
    fun buildsBranchesFromSimplifierFunction() {
        val branches = simplificationTree(42) {
            sequenceOf(
                1,
                2,
                3
            )
        }
            .children.map { it.item }.toList()

        assertEquals(listOf(1, 2, 3), branches)
    }

    @Test
    fun buildsTreeFromSimplifierFunction() {
        val tree =
            simplificationTree(4) { value ->
                when (value) {
                    0 -> emptySequence()
                    1 -> sequenceOf(0)
                    else -> sequenceOf(value / 2, value - 1).distinct()
                }
            }

        assertTreeEquals(
            actual = tree,
            expected = SimplificationTree(
                4,
                sequenceOf(
                    SimplificationTree(
                        2, sequenceOf(
                            SimplificationTree(
                                1, sequenceOf(
                                    SimplificationTree(
                                        0,
                                        emptySequence()
                                    )
                                )
                            )
                        )
                    ),
                    SimplificationTree(
                        3, sequenceOf(
                            SimplificationTree(
                                1, sequenceOf(
                                    SimplificationTree(
                                        0,
                                        emptySequence()
                                    )
                                )
                            ),
                            SimplificationTree(
                                2, sequenceOf(
                                    SimplificationTree(
                                        1, sequenceOf(
                                            SimplificationTree(
                                                0,
                                                emptySequence()
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }
}

