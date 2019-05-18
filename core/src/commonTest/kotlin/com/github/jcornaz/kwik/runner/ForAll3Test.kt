import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.forAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll3Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator.create { it.nextInt() }
    private val testGenerator2 = Generator.create { it.nextDouble() }
    private val testGenerator3 = Generator.create { it.nextLong() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Unit) {
        forAll(testGenerator1, testGenerator2, testGenerator3, iterations, seed) { _, _, _ -> invocation(); true }
    }

    @Test
    fun failFastInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            forAll(testGenerator1, testGenerator2, testGenerator3, iterations = 200) { _, _, _ ->
                invocations++
                false
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()
        val valuesC = mutableSetOf<Long>()

        forAll(testGenerator1, testGenerator2, testGenerator3, seed = 0L) { a, b, c ->
            valuesA += a
            valuesB += b
            valuesC += c
            true
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Triple<Int, Double, Long>>()
        val pass2 = mutableListOf<Triple<Int, Double, Long>>()

        val seed = 123564L

        forAll(testGenerator1, testGenerator2, testGenerator3, seed = seed) { a, b, c ->
            pass1 += Triple(a, b, c)
            true
        }

        forAll(testGenerator1, testGenerator2, testGenerator3, seed = seed) { a, b, c ->
            pass2 += Triple(a, b, c)
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { a: Int, b: Long, c: Double ->
            a is Int && b is Long && c is Double
        }
    }
}
