package com.github.jcornaz.kwik.fuzzer.api.simplifier.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class MapTest {

    @Test
    fun transformsRoot() {
        assertEquals("1", simplestValue(1).map { it.toString() }.root)
    }

    @Test
    fun transformsChildren() {
        assertTreeEquals(
            expected = SimplificationTree(
                "2",
                sequenceOf(
                    simplestValue("3"),
                    simplestValue("4"),
                    SimplificationTree(
                        "5", sequenceOf(
                            simplestValue("42"),
                            simplestValue("43")
                        )
                    ),
                    simplestValue("6")

                )
            ),
            actual = SimplificationTree(
                1,
                sequenceOf(
                    simplestValue(2),
                    simplestValue(3),
                    SimplificationTree(
                        4, sequenceOf(
                            simplestValue(41),
                            simplestValue(42)
                        )
                    ),
                    simplestValue(5)

                )
            ).map { (it + 1).toString() }
        )
    }
}
