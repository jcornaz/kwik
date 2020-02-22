package com.github.jcornaz.kwik.fuzzer.api.simplifier

import com.github.jcornaz.kwik.fuzzer.api.ExperimentalKwikFuzzer
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class SimplificationTest {

    @Test
    fun returnsInitialValueIfThereIsNoSimplerValue() {
        repeat(100) {
            val initialValue = Random.nextInt()
            assertEquals(initialValue, dontSimplify<Int>().findSimplestFalsification(initialValue) { Random.nextBoolean() })
        }
    }

    @Test
    fun returnsInitialValueIfAllDirectSimplerValuesSatisfyTheProperty() {
        val simplifier = object : Simplifier<Int> {
            override fun simplify(value: Int): Sequence<Int> = sequenceOf(1, 2, 3, 4)
        }

        repeat(100) {
            val initialValue = Random.nextInt()
            assertEquals(initialValue, simplifier.findSimplestFalsification(initialValue) { true })
        }
    }

    @Test
    fun returnsFirstSimplestValueThatFalsifyTheProperty() {
        val simplifier = object : Simplifier<Int> {
            override fun simplify(value: Int): Sequence<Int> =
                if (value == 0) emptySequence() else sequenceOf(0, 1, 3)
        }

        repeat(100) {
            val initialValue = Random.nextInt(1, 100)
            assertEquals(0, simplifier.findSimplestFalsification(initialValue) { false })
        }
    }

    @Test
    fun returnsFirstSimplestValueThatFalsifyThePropertyEvenIfItIshNull() {
        val simplifier = object : Simplifier<Int?> {
            override fun simplify(value: Int?): Sequence<Int?> =
                if (value == null) emptySequence() else sequenceOf(null)
        }

        repeat(100) {
            val initialValue = Random.nextInt()
            assertEquals(null, simplifier.findSimplestFalsification(initialValue) { false })
        }
    }

    @Test
    fun returnsIndirectSimplerValuesThatStillFalsify() {
        val simplifier = object : Simplifier<Int> {
            override fun simplify(value: Int): Sequence<Int> = when (value) {
                0 -> emptySequence()
                1 -> sequenceOf(0)
                else -> sequenceOf(value / 2, value - 1)
            }
        }

        repeat(1000) {
            val initialValue = Random.nextInt(100, 200)
            val result = simplifier.findSimplestFalsification(initialValue) { it < 42 }
            assertEquals(42, result)
        }
    }
}
