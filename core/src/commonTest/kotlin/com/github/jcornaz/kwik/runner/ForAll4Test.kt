import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.forAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll4Test : AbstractRunnerTest() {

    private val testGenerator1 = Generator.create { it.nextInt() }
    private val testGenerator2 = Generator.create { it.nextDouble() }
    private val testGenerator3 = Generator.create { it.nextLong() }
    private val testGenerator4 = Generator.create { it.nextFloat() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Unit) {
        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, iterations, seed) { _, _, _, _ ->
            invocation()
            true
        }
    }

    @Test
    fun failFastInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, iterations = 200) { _, _, _, _ ->
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
        val valuesD = mutableSetOf<Float>()

        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, seed = 0L) { a, b, c, d ->
            valuesA += a
            valuesB += b
            valuesC += c
            valuesD += d
            true
        }

        assertTrue(valuesA.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Input>()
        val pass2 = mutableListOf<Input>()

        val seed = 123564L

        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, seed = seed) { a, b, c, d ->
            pass1 += Input(a, b, c, d)
            true
        }

        forAll(testGenerator1, testGenerator2, testGenerator3, testGenerator4, seed = seed) { a, b, c, d ->
            pass2 += Input(a, b, c, d)
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { a: Int, b: Long, c: Double, d: Float ->
            a is Int && b is Long && c is Double && d is Float
        }
    }

    private data class Input(val a: Int, val b: Double, val c: Long, val d: Float)
}
