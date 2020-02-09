package com.github.jcornaz.kwik.fuzzer

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.filter
import com.github.jcornaz.kwik.generator.api.filterNot
import com.github.jcornaz.kwik.generator.api.randomSequence
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalKwikFuzzer
class FilterTest {

    @Test
    fun applyFilterToGenerator() {
        val generator = Generator.create { it.nextInt(0, 10) }

        repeat(10) {
            val seed = Random.nextLong()
            val value = Random.nextInt(0, 10)

            assertEquals(
                generator.filter { it != value }.randomSequence(seed).take(100).toList(),
                generator.toFuzzer().filter { it != value }.generator.randomSequence(seed).take(100).toList()
            )
        }
    }

    @Test
    fun applyFilterNotToGenerator() {
        val generator = Generator.create { it.nextInt(0, 10) }

        repeat(10) {
            val seed = Random.nextLong()
            val value = Random.nextInt(0, 10)

            assertEquals(
                generator.filterNot { it == value }.randomSequence(seed).take(100).toList(),
                generator.toFuzzer().filterNot { it == value }.generator.randomSequence(seed).take(100).toList()
            )
        }
    }
}
