package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.RoseTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import com.github.jcornaz.kwik.fuzzer.api.simplifier.roseTreeOf
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExperimentalKwikFuzzer
class FilterTest {

    @Test
    fun returnsNullIfRootDoesNotSatisfyPredicate() {
        assertNull(roseTreeOf(32).filter { it < 10 })
    }

    @Test
    fun returnsSameIfSatisfyingLeaf() {
        val tree = roseTreeOf(321).filter { it > 0 }
        assertTreeEquals(
            roseTreeOf(
                321
            ), assertNotNull(tree)
        )
    }

    @Test
    fun filterChildren() {
        val tree = RoseTree(
            1,
            sequenceOf(
                2,
                3,
                4,
                5,
                6
            ).map { roseTreeOf(it) })

        assertTreeEquals(
            RoseTree(
                1,
                sequenceOf(
                    3,
                    5
                ).map { roseTreeOf(it) }),
            assertNotNull(tree.filter { it % 2 != 0 })
        )
    }

    @Test
    fun keepsDeepTreeOfChildren() {
        val tree = RoseTree(
            0,
            sequenceOf(
                roseTreeOf(2),
                roseTreeOf(3),
                RoseTree(
                    4,
                    sequenceOf(
                        roseTreeOf(11),
                        roseTreeOf(12)
                    )
                ),
                roseTreeOf(5),
                RoseTree(
                    6,
                    sequenceOf(
                        roseTreeOf(42),
                        roseTreeOf(43)
                    )
                ),
                roseTreeOf(7)
            )
        )

        assertTreeEquals(
            expected = RoseTree(
                0,
                sequenceOf(
                    roseTreeOf(2),
                    RoseTree(
                        4,
                        sequenceOf(
                            roseTreeOf(12)
                        )
                    ),
                    RoseTree(
                        6,
                        sequenceOf(
                            roseTreeOf(42)
                        )
                    )
                )
            ),
            actual = assertNotNull(tree.filter { it % 2 == 0 })
        )
    }
}
