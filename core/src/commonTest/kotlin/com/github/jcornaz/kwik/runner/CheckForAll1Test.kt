import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.checkForAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CheckForAll1Test {

    private val testGenerator = Generator.create { it.nextInt() }

    @Test
    fun zeroIterationShouldFail() {
        assertFailsWith<IllegalArgumentException> {
            checkForAll(testGenerator, iterations = 0) {
                // do nothing
            }
        }
    }

    @Test
    fun invokeThePropertyOncePerIteration() {
        var invocations = 0

        checkForAll(testGenerator) {
            invocations++
        }

        assertEquals(200, invocations)
    }

    @Test
    fun exceptionAreRethrown() {
        val exception = assertFailsWith<CustomException> {
            checkForAll(testGenerator) {
                throw CustomException("hello from exception")
            }
        }

        assertEquals("hello from exception", exception.message)
    }

    @Test
    fun failFastInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            checkForAll(testGenerator, 42) {
                invocations++
                throw AssertionError()
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun evaluateForRandomValues() {
        val values = mutableSetOf<Int>()

        checkForAll(testGenerator, seed = 0L) {
            values += it
        }

        assertTrue(values.size > 190)
    }

    @Test
    fun isPredictable() {
        val gen = Generator.create { it.nextInt() }

        val pass1 = mutableListOf<Int>()
        val pass2 = mutableListOf<Int>()

        val seed = 123564L

        checkForAll(gen, seed = seed) {
            pass1 += it
        }

        checkForAll(gen, seed = seed) {
            pass2 += it
        }

        assertEquals(pass1, pass2)
    }
}
