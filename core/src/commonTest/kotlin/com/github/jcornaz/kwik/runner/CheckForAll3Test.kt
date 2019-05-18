import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.checkForAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckForAll3Test : AbstractRunnerTest() {

    private val generator1 = Generator.create { it.nextInt() }
    private val generator2 = Generator.create { it.nextDouble() }
    private val generator3 = Generator.create { it.nextLong() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Unit) {
        checkForAll(generator1, generator2, generator3, iterations, seed) { _, _, _ -> invocation() }
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()
        val valuesC = mutableSetOf<Long>()

        checkForAll(generator1, generator2, generator3, seed = 0L) { a, b, c ->
            valuesA += a
            valuesB += b
            valuesC += c
        }

        assertTrue(valuesA.size > 190)
        assertTrue(valuesB.size > 190)
        assertTrue(valuesC.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Triple<Int, Double, Long>>()
        val pass2 = mutableListOf<Triple<Int, Double, Long>>()

        val seed = 123564L

        checkForAll(generator1, generator2, generator3, seed = seed) { a, b, c ->
            pass1 += Triple(a, b, c)
        }

        checkForAll(generator1, generator2, generator3, seed = seed) { a, b, c ->
            pass2 += Triple(a, b, c)
        }

        assertEquals(pass1, pass2)
    }
}
