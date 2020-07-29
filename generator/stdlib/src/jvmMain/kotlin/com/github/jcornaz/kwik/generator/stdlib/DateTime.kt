package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withSamples
import java.time.Duration
import java.time.Instant
import java.time.LocalTime

/**
 * Returns a generator of [Instant] between [min] and [max] (inclusive)
 */
fun Generator.Companion.instants(
    min: Instant = Instant.MIN,
    max: Instant = Instant.MAX
): Generator<Instant> {
    require(max >= min) {
        "Max must be equal or after min but min was $min and max was $max"
    }

    val range = min..max

    val samples = mutableListOf(min, max)

    if (Instant.EPOCH in range && Instant.EPOCH !in samples) {
        samples += Instant.EPOCH
    }

    return create { random ->
        Instant.ofEpochSecond(
            random.nextLong(from = min.epochSecond, until = max.epochSecond),
            random.nextLong(from = 0, until = 1_000_000_000L)
        )
    }.withSamples(samples)
}

/**
 * Returns a generator of [Duration] between [min] (inclusive) and [max] (exclusive) durations
 */
fun Generator.Companion.durations(min: Duration, max: Duration): Generator<Duration> {
    require(min <= max) {
        "Min duration is longer than max duration, min was $min and max was $max"
    }
    return create { random ->
        Duration.ofNanos(random.nextLong(from = min.toNanos(), until = max.toNanos()))
    }
}


/**
 * Returns a generator of [LocalTime] between [min] (inclusive) and [max] (exclusive) localTimes
 */

fun Generator.Companion.localTimes(
    min: LocalTime = LocalTime.MIN,
    max: LocalTime = LocalTime.MAX
): Generator<LocalTime> {
    require(max >= min) {
        "Max must be equal or after min but min was $min and max was $max"
    }

    val range = min..max

    val samples = mutableListOf(min, max)

    if (LocalTime.NOON in range && LocalTime.NOON !in samples) {
        samples += LocalTime.NOON
    }

    return create { random ->
        LocalTime.ofNanoOfDay(random.nextLong(min.toNanoOfDay(), max.toNanoOfDay()))
    }.withSamples(samples)
}
