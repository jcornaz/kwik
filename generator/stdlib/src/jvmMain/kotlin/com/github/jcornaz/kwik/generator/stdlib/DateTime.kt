package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withSamples
import java.time.Duration
import java.time.Instant
import java.time.LocalTime

private const val MAX_NANOSECONDS = 1_000_000_000L

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

    // TODO: fix nanoseconds precision
    return create { random ->
        Instant.ofEpochSecond(
            random.nextLong(from = min.epochSecond, until = max.epochSecond),
            random.nextLong(from = 0, until = MAX_NANOSECONDS)
        )
    }.withSamples(samples)
}

/**
 * Returns a generator of [Duration] between [min] and [max] (inclusive)
 */
fun Generator.Companion.durations(
    min: Duration = Duration.ofSeconds(Long.MIN_VALUE),
    max: Duration = Duration.ofSeconds(Long.MAX_VALUE, 999_999_999L)
): Generator<Duration> {
    require(min <= max) {
        "Min must be shorter than max but min was $min and max was $max"
    }

    val range = min..max

    val samples = mutableListOf(min, max)

    if (Duration.ZERO in range && Duration.ZERO !in samples) {
        samples += Duration.ZERO
    }

    // TODO: fix nanoseconds precision
    return create { random ->
        Duration.ofSeconds(
            random.nextLong(from = min.seconds, until = max.seconds),
            random.nextLong(from = 0, until = MAX_NANOSECONDS)
        )
    }.withSamples(samples)
}


/**
 * Returns a generator of [LocalTime] between [min] and [max] (inclusive)
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
