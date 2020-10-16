package com.github.jcornaz.kwik.fuzzer.api

import com.github.jcornaz.kwik.ExperimentalKwikApi
import com.github.jcornaz.kwik.fuzzer.api.simplifier.dontSimplify
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filter
import com.github.jcornaz.kwik.generator.api.filterNot
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikApi
class FuzzerFilterTest {

    @Test
    fun applyFilterToGenerator() {
        val generator = Generator { it.nextInt(0, 10) }

        repeat(10) {
            val seed = Random.nextLong()
            val value = Random.nextInt(0, 10)

            assertEquals(
                generator.filter { it != value }.randomSequence(seed).take(100).toList(),
                generator.toFuzzer(dontSimplify()).filter { it != value }.generator.randomSequence(seed).take(100)
                    .toList()
            )
        }
    }

    @Test
    fun applyFilterNotToGenerator() {
        val generator = Generator { it.nextInt(0, 10) }

        repeat(10) {
            val seed = Random.nextLong()
            val value = Random.nextInt(0, 10)

            assertEquals(
                generator.filterNot { it == value }.randomSequence(seed).take(100).toList(),
                generator.toFuzzer(dontSimplify()).filterNot { it == value }.generator.randomSequence(seed).take(100)
                    .toList()
            )
        }
    }

    @Test
    fun applyFilterToSimplifier() {
        val simplerValues = Generator { it.nextInt() }
            .toFuzzer {
                sequenceOf(1, 2, 3, 4)
            }
            .filter { it % 2 == 0 }
            .simplifier
            .simplify(0).toList()

        assertEquals(listOf(2, 4), simplerValues)
    }

    @Test
    fun applyFilterNotToSimplifier() {
        val simplerValues = Generator { it.nextInt() }
            .toFuzzer {
                sequenceOf(1, 2, 3, 4)
            }
            .filterNot { it % 2 == 0 }
            .simplifier
            .simplify(0).toList()

        assertEquals(listOf(1, 3), simplerValues)
    }
}
