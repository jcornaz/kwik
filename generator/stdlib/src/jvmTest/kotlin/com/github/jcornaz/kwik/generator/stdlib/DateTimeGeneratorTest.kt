package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.util.*

class DateGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Date> = Generator.dates(Date(0), Date())
}

class DurationGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Duration> = Generator.durations(Duration.ZERO, Duration.ofDays(100))
}

class InstantGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Instant> = Generator.instants(Instant.EPOCH, Instant.now())
}

class LocalTimeGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<LocalTime> = Generator.localTimes()
}