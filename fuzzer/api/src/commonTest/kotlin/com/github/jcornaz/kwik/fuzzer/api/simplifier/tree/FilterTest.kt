package com.github.jcornaz.kwik.fuzzer.api.simplifier.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExperimentalKwikFuzzer
class FilterTest {

    @Test
    fun returnsNullIfRootDoesNotSatisfyPredicate() {
        assertNull(simplestValue(32).filter { it < 10 })
    }

    @Test
    fun returnsSameIfSatisfyingLeaf() {
        val tree = simplestValue(321).filter { it > 0 }
        assertTreeEquals(simplestValue(321), assertNotNull(tree))
    }

    @Test
    fun filterChildren() {
        val tree = SimplificationTree(
            1,
            sequenceOf(
                2,
                3,
                4,
                5,
                6
            ).map { simplestValue(it) })

        assertTreeEquals(
            SimplificationTree(
                1,
                sequenceOf(
                    3,
                    5
                ).map { simplestValue(it) }),
            assertNotNull(tree.filter { it % 2 != 0 })
        )
    }

    @Test
    fun keepsDeepTreeOfChildren() {
        val tree = SimplificationTree(
            0,
            sequenceOf(
                simplestValue(2),
                simplestValue(3),
                SimplificationTree(
                    4,
                    sequenceOf(
                        simplestValue(11),
                        simplestValue(12)
                    )
                ),
                simplestValue(5),
                SimplificationTree(
                    6,
                    sequenceOf(
                        simplestValue(42),
                        simplestValue(43)
                    )
                ),
                simplestValue(7)
            )
        )

        assertTreeEquals(
            expected = SimplificationTree(
                0,
                sequenceOf(
                    simplestValue(2),
                    SimplificationTree(
                        4,
                        sequenceOf(
                            simplestValue(12)
                        )
                    ),
                    SimplificationTree(
                        6,
                        sequenceOf(
                            simplestValue(42)
                        )
                    )
                )
            ),
            actual = assertNotNull(tree.filter { it % 2 == 0 })
        )
    }
}
