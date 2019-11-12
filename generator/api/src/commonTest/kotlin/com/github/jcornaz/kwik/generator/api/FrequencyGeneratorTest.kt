package com.github.jcornaz.kwik.generator.api

import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FrequencyGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Int> = Generator.frequency(
        1.0 to Generator.create { it.nextInt(0, 100) },
        2.0 to Generator.create { it.nextInt(-100, 0) }
    )

    @Test
    fun failsWithEmptyList() {
        assertFailsWith<IllegalArgumentException> {
            Generator.frequency<Int>()
        }
    }

    @Test
    fun canGenerateFromSingleSource() {
        val source = Generator.create { it.nextInt() }
        val result = Generator.frequency(42.0 to source)

        assertEquals(
            source.randomSequence(0).take(100).toList(),
            result.randomSequence(0).take(100).toList()
        )
    }

    @Test
    fun generateFromAllSources() {
        val generator = Generator.frequency(
            1.0 to Generator.create { it.nextInt(0, 10) },
            1.0 to Generator.create { it.nextInt(100, 200) }
        )

        val randomValues = generator.randomSequence(0).take(1000)
        assertTrue(randomValues.any { it in 0 until 10 })
        assertTrue(randomValues.any { it in 100 until 200 })
    }

    @Test
    fun generateUsingGivenProbability() {
        val generator = Generator.frequency(
            0.5 to Generator.create { it.nextInt(0, 50) },
            0.2 to Generator.create { it.nextInt(50, 70) },
            0.3 to Generator.create { it.nextInt(70, 100) }
        )

        val sequence = generator.randomSequence(0).take(1000)

        // Note: expected ranges represent 5 times the standard deviation
        assertTrue(sequence.count { it < 50 } in 420..580)
        assertTrue(sequence.count { it in 50 until 70 } in 130..270)
        assertTrue(sequence.count { it > 70 } in 225..375)
    }
}
