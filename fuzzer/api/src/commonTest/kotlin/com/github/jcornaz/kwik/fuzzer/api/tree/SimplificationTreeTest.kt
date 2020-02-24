package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.RoseTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.buildRoseTree
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class SimplificationTreeTest {

    @Test
    fun rootIsGivenValue() {
        repeat(100) {
            val value = Random.nextInt()
            assertEquals(value, buildRoseTree(
                value
            ) { emptySequence() }.item)
        }
    }

    @Test
    fun buildsBranchesFromSimplifierFunction() {
        val branches = buildRoseTree(42) {
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
            buildRoseTree(4) { value ->
                when (value) {
                    0 -> emptySequence()
                    1 -> sequenceOf(0)
                    else -> sequenceOf(value / 2, value - 1).distinct()
                }
            }

        assertTreeEquals(
            actual = tree,
            expected = RoseTree(
                4,
                sequenceOf(
                    RoseTree(
                        2, sequenceOf(
                            RoseTree(
                                1, sequenceOf(
                                    RoseTree(
                                        0,
                                        emptySequence()
                                    )
                                )
                            )
                        )
                    ),
                    RoseTree(
                        3, sequenceOf(
                            RoseTree(
                                1, sequenceOf(
                                    RoseTree(
                                        0,
                                        emptySequence()
                                    )
                                )
                            ),
                            RoseTree(
                                2, sequenceOf(
                                    RoseTree(
                                        1, sequenceOf(
                                            RoseTree(
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

