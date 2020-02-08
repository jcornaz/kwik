package com.github.jcornaz.kwik.evaluator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class DefaultSeedPropertyTest {

    @Test
    fun nextSeedReturnsDifferentValueIfNoProperty() {
        withSystemProperty("kwik.seed", null) {
            assertNotEquals(nextSeed(), nextSeed())
        }
    }

    @Test
    fun nextSeedReturnsValueFromProperty() {
        withSystemProperty("kwik.seed", "314") {
            assertEquals(314, nextSeed())
            assertEquals(nextSeed(), nextSeed())
        }
    }

    @Test
    fun forAll1UseSeedInPropertyIfAny() {
        withSystemProperty("kwik.seed", "42") {
            val exception1 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int -> false }
            }

            val exception2 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int -> false }
            }

            assertEquals(42, exception1.seed)
            assertEquals(42, exception2.seed)
            assertEquals(exception1.arguments, exception2.arguments)
        }
    }

    @Test
    fun forAll2UseSeedInPropertyIfAny() {
        withSystemProperty("kwik.seed", "3") {
            val exception1 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int, _: Long -> false }
            }

            val exception2 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int, _: Long -> false }
            }

            assertEquals(3, exception1.seed)
            assertEquals(3, exception2.seed)
            assertEquals(exception1.arguments, exception2.arguments)
        }
    }

    @Test
    fun forAll3UseSeedInPropertyIfAny() {
        withSystemProperty("kwik.seed", "44") {
            val exception1 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int, _: Long, _: Double -> false }
            }

            val exception2 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int, _: Long, _: Double -> false }
            }

            assertEquals(44, exception1.seed)
            assertEquals(44, exception2.seed)
            assertEquals(exception1.arguments, exception2.arguments)
        }
    }

    @Test
    fun forAll4UseSeedInPropertyIfAny() {
        withSystemProperty("kwik.seed", "12") {
            val exception1 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int, _: Long, _: Double, _: Boolean -> false }
            }

            val exception2 = assertFailsWith<FalsifiedPropertyError> {
                forAll { _: Int, _: Long, _: Double, _: Boolean -> false }
            }

            assertEquals(12, exception1.seed)
            assertEquals(12, exception2.seed)
            assertEquals(exception1.arguments, exception2.arguments)
        }
    }
}
