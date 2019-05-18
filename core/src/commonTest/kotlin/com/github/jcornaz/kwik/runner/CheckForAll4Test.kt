import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.checkForAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckForAll4Test : AbstractRunnerTest() {

    private val generator1 = Generator.create { it.nextInt() }
    private val generator2 = Generator.create { it.nextDouble() }
    private val generator3 = Generator.create { it.nextLong() }
    private val generator4 = Generator.create { it.nextFloat() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Unit) {
        checkForAll(generator1, generator2, generator3, generator4, iterations, seed) { _, _, _, _ -> invocation() }
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()
        val valuesC = mutableSetOf<Long>()
        val valuesD = mutableSetOf<Float>()

        checkForAll(generator1, generator2, generator3, generator4, seed = 0L) { a, b, c, d ->
            valuesA += a
            valuesB += b
            valuesC += c
            valuesD += d
        }

        assertTrue(valuesA.size > 190)
        assertTrue(valuesB.size > 190)
        assertTrue(valuesC.size > 190)
        assertTrue(valuesD.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Input>()
        val pass2 = mutableListOf<Input>()

        val seed = 123564L

        checkForAll(generator1, generator2, generator3, generator4, seed = seed) { a, b, c, d ->
            pass1 += Input(a, b, c, d)
        }

        checkForAll(generator1, generator2, generator3, generator4, seed = seed) { a, b, c, d ->
            pass2 += Input(a, b, c, d)
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        checkForAll { a: Int, b: Double, c: Long, d: Float ->
            assertTrue(a is Int)
            assertTrue(b is Double)
            assertTrue(c is Long)
            assertTrue(d is Float)
        }
    }

    private data class Input(val a: Int, val b: Double, val c: Long, val d: Float)
}
