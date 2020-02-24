package com.github.jcornaz.kwik.fuzzer.api.tree

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import com.github.jcornaz.kwik.fuzzer.api.simplifier.buildRoseTree
import com.github.jcornaz.kwik.fuzzer.api.simplifier.findSimplestFalsification
import com.github.jcornaz.kwik.fuzzer.api.simplifier.roseTreeOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class SimplificationTest {

    @Test
    fun returnsInitialValueIfThereIsNoSimplerValue() {
        repeat(100) {
            val initialValue = Random.nextInt()
            assertEquals(initialValue, roseTreeOf(
                initialValue
            ).findSimplestFalsification { Random.nextBoolean() })
        }
    }

    @Test
    fun returnsInitialValueIfAllDirectSimplerValuesSatisfyTheProperty() {
        repeat(100) {
            val initialValue = Random.nextInt()

            val tree =
                buildRoseTree(initialValue) {
                    sequenceOf(1, 2, 3, 4)
                }

            assertEquals(initialValue, tree.findSimplestFalsification { true })
        }
    }

    @Test
    fun returnsFirstSimplestValueThatFalsifyTheProperty() {

        repeat(100) {
            val initialValue = Random.nextInt(1, 100)
            val tree =
                buildRoseTree(initialValue) { value ->
                    if (value == 0) emptySequence() else sequenceOf(0, 1, 3)
                }
            assertEquals(0, tree.findSimplestFalsification { false })
        }
    }

    @Test
    fun returnsFirstSimplestValueThatFalsifyThePropertyEvenIfItIshNull() {
        repeat(100) {
            val initialValue = Random.nextInt()
            val tree =
                buildRoseTree<Int?>(
                    initialValue
                ) { value ->
                    if (value == null) emptySequence() else sequenceOf(null)
                }
            assertEquals(null, tree.findSimplestFalsification { false })
        }
    }

    @Test
    fun returnsIndirectSimplerValuesThatStillFalsify() {

        repeat(1000) {
            val initialValue = Random.nextInt(100, 200)
            val tree =
                buildRoseTree(initialValue) { value ->
                    when (value) {
                        0 -> emptySequence()
                        1 -> sequenceOf(0)
                        else -> sequenceOf(value / 2, value - 1)
                    }
                }
            val result = tree.findSimplestFalsification { it < 42 }
            assertEquals(42, result)
        }
    }
}
