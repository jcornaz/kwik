import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.forAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll1Test {

    private val testGenerator = Generator.create { it.nextInt() }

    @Test
    fun zeroIterationShouldFail() {
        assertFailsWith<IllegalArgumentException> {
            forAll(testGenerator, iterations = 0) { true }
        }
    }

    @Test
    fun invokeThePropertyOncePerIteration() {
        var invocations = 0

        forAll(testGenerator) {
            invocations++
            true
        }

        assertEquals(200, invocations)
    }

    @Test
    fun exceptionAreRethrown() {
        val exception = assertFailsWith<CustomException> {
            forAll(testGenerator) {
                throw CustomException("hello from exception")
            }
        }

        assertEquals("hello from exception", exception.message)
    }

    @Test
    fun failFastInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            forAll(testGenerator, 42) {
                invocations++
                false
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun evaluateForRandomValues() {
        val values = mutableSetOf<Int>()

        forAll(testGenerator, seed = 0L) {
            values += it
            true
        }

        assertTrue(values.size > 190)
    }

    @Test
    fun isPredictable() {
        val gen = Generator.create { it.nextInt() }

        val pass1 = mutableListOf<Int>()
        val pass2 = mutableListOf<Int>()

        val seed = 123564L

        forAll(gen, seed = seed) {
            pass1 += it
            true
        }

        forAll(gen, seed = seed) {
            pass2 += it
            true
        }

        assertEquals(pass1, pass2)
    }
}

