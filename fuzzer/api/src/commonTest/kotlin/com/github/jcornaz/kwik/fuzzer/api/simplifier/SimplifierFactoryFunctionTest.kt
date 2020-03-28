package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.tree.assertTreeEquals
import kotlin.random.Random
import kotlin.test.Test

@ExperimentalKwikFuzzer
class SimplifierFactoryFunctionTest {

    @Test
    fun returnsASimplifierCapableOfBuildingTreeBasedOnTheGivenFunction() {
        val fct = { value: Int ->
            when {
                value == 42 -> emptySequence()
                value < 42 -> sequenceOf(value + 1)
                else -> sequenceOf(value - 1)
            }
        }

        val simplifier = simplifier(fct)

        repeat(1000) {
            val root = Random.nextInt(-100, 100)

            assertTreeEquals(
                expected = simplificationTree(root, fct),
                actual = SimplificationTree(root, simplifier.simplify(root))
            )
        }
    }
}