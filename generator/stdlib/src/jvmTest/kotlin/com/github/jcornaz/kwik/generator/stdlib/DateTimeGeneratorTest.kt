package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DurationGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Duration> = Generator.durations(Duration.ZERO, Duration.ofDays(100))

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.durations(Duration.ofSeconds(1), Duration.ZERO)
        }
    }
}

class InstantGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Instant> = Generator.instants(Instant.EPOCH, Instant.now())

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.instants(Instant.now(), Instant.EPOCH)
        }
    }
}

class LocalTimeGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<LocalTime> = Generator.localTimes()

    @Test
    fun failForInvalidMax() {
        assertFailsWith<IllegalArgumentException> {
            Generator.localTimes(LocalTime.MAX, LocalTime.MIN)
        }
    }
}