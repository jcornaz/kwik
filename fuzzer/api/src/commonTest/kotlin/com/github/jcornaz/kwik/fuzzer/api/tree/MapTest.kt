package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.RoseTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.map
import com.github.jcornaz.kwik.fuzzer.api.simplifier.roseTreeOf
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class MapTest {

    @Test
    fun transformsRoot() {
        assertEquals("1", roseTreeOf(1).map { it.toString() }.item)
    }

    @Test
    fun transformsChildren() {
        assertTreeEquals(
            expected = RoseTree(
                "2",
                sequenceOf(
                    roseTreeOf("3"),
                    roseTreeOf("4"),
                    RoseTree(
                        "5", sequenceOf(
                            roseTreeOf("42"),
                            roseTreeOf("43")
                        )
                    ),
                    roseTreeOf("6")

                )
            ),
            actual = RoseTree(
                1,
                sequenceOf(
                    roseTreeOf(2),
                    roseTreeOf(3),
                    RoseTree(
                        4, sequenceOf(
                            roseTreeOf(41),
                            roseTreeOf(42)
                        )
                    ),
                    roseTreeOf(5)

                )
            ).map { (it + 1).toString() }
        )
    }
}
