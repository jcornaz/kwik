package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.SimplificationTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.map
import com.github.jcornaz.kwik.fuzzer.api.simplifier.simplificationTreeOf
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class MapTest {

    @Test
    fun transformsRoot() {
        assertEquals("1", simplificationTreeOf(1).map { it.toString() }.item)
    }

    @Test
    fun transformsChildren() {
        assertTreeEquals(
            expected = SimplificationTree(
                "2",
                sequenceOf(
                    simplificationTreeOf("3"),
                    simplificationTreeOf("4"),
                    SimplificationTree(
                        "5", sequenceOf(
                            simplificationTreeOf("42"),
                            simplificationTreeOf("43")
                        )
                    ),
                    simplificationTreeOf("6")

                )
            ),
            actual = SimplificationTree(
                1,
                sequenceOf(
                    simplificationTreeOf(2),
                    simplificationTreeOf(3),
                    SimplificationTree(
                        4, sequenceOf(
                            simplificationTreeOf(41),
                            simplificationTreeOf(42)
                        )
                    ),
                    simplificationTreeOf(5)

                )
            ).map { (it + 1).toString() }
        )
    }
}
