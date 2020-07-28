package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import java.time.Duration
import java.time.Instant
import java.time.LocalTime

private const val DAY_NANOSECONDS = 86400000000000L

/**
 * Returns a generator of [Instant] between [min] (inclusive) and [max] (exclusive) instants
 */
fun Generator.Companion.instants(min: Instant, max: Instant): Generator<Instant> {
    require(min.isBefore(max)) {
        "Max must be after min but min was $min and max was $max"
    }
    return create { random ->
        Instant.ofEpochMilli(random.nextLong(from = min.toEpochMilli(), until = max.toEpochMilli()))
    }
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
 * Returns a generator of [LocalTime]
 */
fun Generator.Companion.localTimes(): Generator<LocalTime> =
    create { random -> LocalTime.ofNanoOfDay(random.nextLong(0, DAY_NANOSECONDS)) }

/**
 * Returns a generator of [Instant] between [min] (inclusive) and [max] (exclusive) instants
 */
fun Generator.Companion.localTimes(min: LocalTime, max: LocalTime): Generator<LocalTime> {
    require(min.isBefore(max)) {
        "Max must be after than min but min was $min and max was $max"
    }
    return create { random ->
        LocalTime.ofNanoOfDay(random.nextLong(min.toNanoOfDay(), max.toNanoOfDay()))
    }
}
