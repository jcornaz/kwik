package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.filter
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplificationTreeOf
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExperimentalKwikFuzzer
class FilterTest {

    @Test
    fun returnsNullIfRootDoesNotSatisfyPredicate() {
        assertNull(simplificationTreeOf(32).filter { it < 10 })
    }

    @Test
    fun returnsSameIfSatisfyingLeaf() {
        val tree = simplificationTreeOf(321).filter { it > 0 }
        assertTreeEquals(
            simplificationTreeOf(
                321
            ), assertNotNull(tree)
        )
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
            ).map { simplificationTreeOf(it) })

        assertTreeEquals(
            SimplificationTree(
                1,
                sequenceOf(
                    3,
                    5
                ).map { simplificationTreeOf(it) }),
            assertNotNull(tree.filter { it % 2 != 0 })
        )
    }

    @Test
    fun keepsDeepTreeOfChildren() {
        val tree = SimplificationTree(
            0,
            sequenceOf(
                simplificationTreeOf(2),
                simplificationTreeOf(3),
                SimplificationTree(
                    4,
                    sequenceOf(
                        simplificationTreeOf(11),
                        simplificationTreeOf(12)
                    )
                ),
                simplificationTreeOf(5),
                SimplificationTree(
                    6,
                    sequenceOf(
                        simplificationTreeOf(42),
                        simplificationTreeOf(43)
                    )
                ),
                simplificationTreeOf(7)
            )
        )

        assertTreeEquals(
            expected = SimplificationTree(
                0,
                sequenceOf(
                    simplificationTreeOf(2),
                    SimplificationTree(
                        4,
                        sequenceOf(
                            simplificationTreeOf(12)
                        )
                    ),
                    SimplificationTree(
                        6,
                        sequenceOf(
                            simplificationTreeOf(42)
                        )
                    )
                )
            ),
            actual = assertNotNull(tree.filter { it % 2 == 0 })
        )
    }
}
