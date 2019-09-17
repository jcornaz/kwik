package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.random.Random
import kotlin.test.*

class SequenceGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Sequence<Int>> = Generator.sequences()

    @Test
    fun generateOfAllSizeInGivenRange() {
        val sizes = mutableSetOf<Int>()

        repeat(100) {
            val size = Generator.sequences<Int>(minSize = 3, maxSize = 12).generate(Random).count()
            assertTrue(size in 3..12)
            sizes += size
        }

        assertEquals((3..12).toSet(), sizes)
    }

    @Test
    override fun isPredictable() {
        repeat(100) {
            val seed = Random.nextLong()

            assertEquals(
                generator.generate(Random(seed)).toList(),
                generator.generate(Random(seed)).toList()
            )
        }
    }

    @Test
    override fun isRandom() {
        val generation1 = (0 until 100).map { generator.generate(Random).toList() }
        val generation2 = (0 until 100).map { generator.generate(Random).toList() }

        assertNotEquals(generation1, generation2)
    }

    @Test
    fun generateAllElementsOfElementGenerator() {
        val elemGen = Generator.create { it.nextInt(0, 10) }
        val elements = mutableSetOf<Int>()

        repeat(100) {
            Generator.sequences(elemGen).generate(Random).forEach {
                assertTrue(it in 0 until 10)
                elements += it
            }
        }

        assertEquals((0 until 10).toSet(), elements)
    }

    @Test
    fun hasSameSamplesThanListGenerator() {
        val elemGen = object : Generator<Int> {
            override val samples: Set<Int> = setOf(1, 2, 3)

            override fun generate(random: Random): Int = random.nextInt()
        }

        repeat(200) {
            val minSize = Random.nextInt(0, 2)
            val maxSize = Random.nextInt(minSize, 3)

            assertEquals(
                Generator.lists(elemGen, minSize, maxSize).samples,
                Generator.sequences(elemGen, minSize, maxSize).samples.mapTo(HashSet()) { it.toList() }
            )
        }
    }

    @Test
    fun failsWithMinSizeSmallerThan0() {
        assertFailsWith<IllegalArgumentException> {
            Generator.sequences<Int>(minSize = -1)
        }
    }

    @Test
    fun failsWithMaxSizeSmallerThanMinSize() {
        assertFailsWith<IllegalArgumentException> {
            Generator.sequences<Int>(minSize = 3, maxSize = 2)
        }
    }

    @Test
    fun bigMinSizeIsPossible() {
        assertEquals(1000, Generator.sequences(Generator.ints(), minSize = 1000).generate(Random).count())
    }

    @Test
    fun generatedSequencesSupportToString() {
        val sequence = generator.generate(Random)

        assertEquals(sequence.toList().toString(), sequence.toString())
    }
}

class NonEmptySequenceGeneratorTest {

    @Test
    fun generateSameThanSequenceGeneratorWithMinSizeOf1() {
        repeat(100) {
            val maxSize = Random.nextInt(1, 10)
            val seed = Random.nextLong()
            assertEquals(
                Generator.sequences<Int>(1, maxSize).generate(Random(seed)).toList(),
                Generator.nonEmptySequences<Int>(maxSize).generate(Random(seed)).toList()
            )
        }
    }

    @Test
    fun generateSameThanSequenceGeneratorWithMinSizeOf1AndElementGen() {
        repeat(100) {
            val maxSize = Random.nextInt(1, 10)
            val seed = Random.nextLong()
            assertEquals(
                Generator.sequences(Generator.ints(), 1, maxSize).generate(Random(seed)).toList(),
                Generator.nonEmptySequences(Generator.ints(), maxSize).generate(Random(seed)).toList()
            )
        }
    }

    @Test
    @Ignore
    fun samplesAreSameThanSequenceGeneratorWithMinSizeOf1() {
        repeat(100) {
            val maxSize = Random.nextInt(1, 10)
            assertEquals(
                Generator.sequences<Int>(1, maxSize).samples.map { it.toList() },
                Generator.nonEmptySequences<Int>(maxSize).samples.map { it.toList() }
            )
        }
    }

    @Test
    @Ignore
    fun samplesAreSameThanSequenceGeneratorWithMinSizeOf1AndElementGen() {
        repeat(100) {
            val maxSize = Random.nextInt(1, 10)
            assertEquals(
                Generator.sequences(Generator.ints(), 1, maxSize).samples.map { it.toList() },
                Generator.nonEmptySequences(Generator.ints(), maxSize).samples.map { it.toList() }
            )
        }
    }
}
