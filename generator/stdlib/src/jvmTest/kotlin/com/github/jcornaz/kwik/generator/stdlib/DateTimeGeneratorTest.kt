package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private const val MAX_NANOSECONDS = 999_999_999L
// can be replaced by LocalDate.EPOCH with Java 9
private val EPOCH: LocalDate = LocalDate.ofEpochDay(0)
private val EPOCH_WITH_TIME: LocalDateTime = EPOCH.atStartOfDay()

class DurationGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Duration> = Generator.durations(Duration.ZERO, Duration.ofDays(100))

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.durations(Duration.ofSeconds(1), Duration.ZERO)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        val min = Duration.ofMinutes(-1)
        val max = Duration.ofMinutes(1)
        assertTrue(Generator.durations(min = min, max = max).randomSequence(0).take(50).all { it >= min && it <= max })
    }

    @Test
    fun produceInsideGivenNanosecondsRange() {
        val min = Duration.ofSeconds(0, 500)
        val max = Duration.ofSeconds(0, 1000)
        assertTrue(Generator.durations(min = min, max = max).randomSequence(0).take(50).all { it >= min && it <= max })
    }

    @Test
    fun doNotProduceZeroIfNotInRange() {
        val min = Duration.ofMinutes(1)
        assertTrue(Generator.durations(min = min).randomSequence(0).take(50).none { it == Duration.ZERO })
    }

    @Test
    fun doNotProduceGlobalMaxIfNotInRange() {
        val max = Duration.ZERO
        assertTrue(Generator.durations(max = max).randomSequence(0).take(50).none { it == Duration.ofSeconds(Long.MAX_VALUE, MAX_NANOSECONDS) })
    }

    @Test
    fun doNotProduceGlobalMinIfNotInRange() {
        val min = Duration.ZERO
        assertTrue(Generator.durations(min = min).randomSequence(0).take(50).none { it == Duration.ofSeconds(Long.MIN_VALUE) })
    }

    @Test
    fun generateZero() {
        assertTrue(Generator.durations().randomSequence(0).take(50).any { it == Duration.ZERO })
    }

    @Test
    fun generateGlobalMax() {
        assertTrue(Generator.durations().randomSequence(0).take(50).any { it == Duration.ofSeconds(Long.MAX_VALUE, MAX_NANOSECONDS) })
    }

    @Test
    fun generateGlobalMin() {
        assertTrue(Generator.durations().randomSequence(0).take(50).any { it == Duration.ofSeconds(Long.MIN_VALUE) })
    }

    @Test
    fun generateMax() {
        val max = Duration.ofSeconds(1)
        assertTrue(Generator.durations(max = max).randomSequence(0).take(50).any { it == max })
    }

    @Test
    fun generateMin() {
        val min = Duration.ofSeconds(1)
        assertTrue(Generator.durations(min = min).randomSequence(0).take(50).any { it == min })
    }
}

class InstantGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<Instant> = Generator.instants(Instant.EPOCH, Instant.now())

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.instants(Instant.now(), Instant.EPOCH)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        val min = Instant.now()
        val max = min.plusSeconds(50)
        assertTrue(Generator.instants(min = min, max = max).randomSequence(0).take(50).all { it >= min && it <= max })
    }

    @Test
    fun produceInsideGivenNanosecondsRange() {
        val min = Instant.EPOCH
        val max = min.plusNanos(500)
        assertTrue(Generator.instants(min = min, max = max).randomSequence(0).take(50).all { it >= min && it <= max })
    }

    @Test
    fun doNotProduceEpochIfNotInRange() {
        val min = Instant.now()
        val max = min.plusSeconds(50)
        assertTrue(Generator.instants(min = min, max = max).randomSequence(0).take(50).none { it == Instant.EPOCH })
    }

    @Test
    fun doNotProduceFarPastIfNotInRange() {
        val min = Instant.now()
        val max = min.plusSeconds(50)
        assertTrue(Generator.instants(min = min, max = max).randomSequence(0).take(50).none { it == Instant.MIN })
    }

    @Test
    fun doNotProduceFarFutureIfNotInRange() {
        val min = Instant.now()
        val max = min.plusSeconds(50)
        assertTrue(Generator.instants(min = min, max = max).randomSequence(0).take(50).none { it == Instant.MAX })
    }

    @Test
    fun generateEpoch() {
        assertTrue(Generator.instants().randomSequence(0).take(50).any { it == Instant.EPOCH })
    }

    @Test
    fun generateFarPast() {
        assertTrue(Generator.instants().randomSequence(0).take(50).any { it == Instant.MIN })
    }

    @Test
    fun generateFarFuture() {
        assertTrue(Generator.instants().randomSequence(0).take(50).any { it == Instant.MAX })
    }

    @Test
    fun generateMax() {
        val min = Instant.now()
        val max = min.plusSeconds(50)
        assertTrue(Generator.instants(min = min, max = max).randomSequence(0).take(50).any { it == max })
    }

    @Test
    fun generateMin() {
        val min = Instant.now()
        val max = min.plusSeconds(50)
        assertTrue(Generator.instants(min = min, max = max).randomSequence(0).take(50).any { it == min })
    }
}

class LocalTimeGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<LocalTime> = Generator.localTimes()

    @Test
    fun failForInvalidRange() {
        assertFailsWith<IllegalArgumentException> {
            Generator.localTimes(LocalTime.MAX, LocalTime.MIN)
        }
    }

    @Test
    fun produceInsideGivenRange() {
        val min = LocalTime.NOON
        val max = min.plusHours(2)
        assertTrue(Generator.localTimes(min = min, max = max).randomSequence(0).take(50).all { it >= min && it <= max })
    }

    @Test
    fun doNotProduceNoonIfNotInRange() {
        val min = LocalTime.NOON.plusHours(2)
        assertTrue(Generator.localTimes(min = min).randomSequence(0).take(50).none { it == LocalTime.NOON })
    }

    @Test
    fun doNotProduceGlobalMaxIfNotInRange() {
        val max = LocalTime.MAX.minusHours(1)
        assertTrue(Generator.localTimes(max = max).randomSequence(0).take(50).none { it == LocalTime.MAX })
    }

    @Test
    fun doNotProduceGlobalMinIfNotInRange() {
        val min = LocalTime.MIN.plusHours(1)
        assertTrue(Generator.localTimes(min = min).randomSequence(0).take(50).none { it == LocalTime.MIN })
    }

    @Test
    fun generateNoon() {
        assertTrue(Generator.localTimes().randomSequence(0).take(50).any { it == LocalTime.NOON })
    }

    @Test
    fun generateGlobalMax() {
        assertTrue(Generator.localTimes().randomSequence(0).take(50).any { it == LocalTime.MAX })
    }


    @Test
    fun generateGlobalMin() {
        assertTrue(Generator.localTimes().randomSequence(0).take(50).any { it == LocalTime.MIN })
    }

    @Test
    fun generateMax() {
        val max = LocalTime.NOON.plusHours(1)
        assertTrue(Generator.localTimes(max = max).randomSequence(0).take(50).any { it == max })
    }

    @Test
    fun generateMin() {
        val min = LocalTime.NOON.plusHours(1)
        assertTrue(Generator.localTimes(min = min).randomSequence(0).take(50).any { it == min })
    }
}

class LocalDateGeneratorTest : AbstractGeneratorTest() {

    override val generator: Generator<LocalDate> = Generator.localDates()

    @Test
    fun `fail for invalid range`() {
        assertFailsWith<IllegalArgumentException> {
            Generator.localDates(LocalDate.MAX, LocalDate.MIN)
        }
    }

    @Test
    fun `produce inside given range`() {
        val min = EPOCH
        val max = min.plusWeeks(2)
        assertTrue(Generator.localDates(min = min, max = max).randomSequence(0).take(50).all { it >= min && it <= max })
    }

    @Test
    fun `do not produce EPOCH if not in range`() {
        val min = EPOCH.plusDays(2)
        val max = EPOCH.plusWeeks(2)
        assertTrue(Generator.localDates(min = min, max = max).randomSequence(0).take(50).none { it == EPOCH })
    }

    @Test
    fun `do not produce global max if not in range`() {
        val max = LocalDate.MAX.minusDays(1)
        assertTrue(Generator.localDates(max = max).randomSequence(0).take(50).none { it == LocalDate.MAX })
    }

    @Test
    fun `do not produce global min if not in range`() {
        val min = LocalDate.MIN.plusDays(1)
        assertTrue(Generator.localDates(min = min).randomSequence(0).take(50).none { it == LocalDate.MIN })
    }

    @Test
    fun `generate epoch`() {
        assertTrue(Generator.localDates().randomSequence(0).take(50).any { it == EPOCH })
    }

    @Test
    fun `generate global max`() {
        assertTrue(Generator.localDates().randomSequence(0).take(50).any { it == LocalDate.MAX })
    }

    @Test
    fun `generate global min`() {
        assertTrue(Generator.localDates().randomSequence(0).take(50).any { it == LocalDate.MIN })
    }

    @Test
    fun `generate max`() {
        val max = EPOCH.plusDays(1)
        assertTrue(Generator.localDates(max = max).randomSequence(0).take(50).any { it == max })
    }

    @Test
    fun `generate min`() {
        val min = EPOCH.plusDays(1)
        assertTrue(Generator.localDates(min = min).randomSequence(0).take(50).any { it == min })
    }
}

class LocalDateTimeGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<LocalDateTime> = Generator.localDateTimes()

    @Test
    fun `fail for invalid range`() {
        assertFailsWith<IllegalArgumentException> {
            Generator.localDateTimes(LocalDateTime.MAX, LocalDateTime.MIN)
        }
    }

    @Test
    fun `produce inside given range`() {
        val min = EPOCH_WITH_TIME
        val max = EPOCH_WITH_TIME.plusDays(2)
        assertTrue(Generator.localDateTimes(min = min, max = max).randomSequence(0).take(50).all { it >= min && it <= max })
    }

    @Test
    fun `do not produce epoch if not in range`() {
        val min = EPOCH_WITH_TIME.plusHours(2)
        assertTrue(Generator.localDateTimes(min = min).randomSequence(0).take(50).none { it == EPOCH_WITH_TIME })
    }

    @Test
    fun `do not produce global max if not in range`() {
        val max = LocalDateTime.MAX.minusHours(1)
        assertTrue(Generator.localDateTimes(max = max).randomSequence(0).take(50).none { it == LocalDateTime.MAX })
    }

    @Test
    fun `do not produce global min if not in range`() {
        val min = LocalDateTime.MIN.plusHours(1)
        assertTrue(Generator.localDateTimes(min = min).randomSequence(0).take(50).none { it == LocalDateTime.MIN })
    }

    @Test
    fun `generate epoch`() {
        assertTrue(Generator.localDateTimes().randomSequence(0).take(50).any { it == EPOCH_WITH_TIME })
    }

    @Test
    fun `generate global max`() {
        assertTrue(Generator.localDateTimes().randomSequence(0).take(50).any { it == LocalDateTime.MAX })
    }


    @Test
    fun `generate global min`() {
        assertTrue(Generator.localDateTimes().randomSequence(0).take(50).any { it == LocalDateTime.MIN })
    }

    @Test
    fun `generate max`() {
        val max = EPOCH_WITH_TIME.plusHours(1)
        assertTrue(Generator.localDateTimes(max = max).randomSequence(0).take(50).any { it == max })
    }

    @Test
    fun `generate min`() {
        val min = EPOCH_WITH_TIME.plusHours(1)
        assertTrue(Generator.localDateTimes(min = min).randomSequence(0).take(50).any { it == min })
    }
}