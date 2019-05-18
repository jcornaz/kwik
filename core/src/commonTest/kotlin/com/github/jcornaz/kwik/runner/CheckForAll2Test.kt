import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.checkForAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckForAll2Test : AbstractRunnerTest() {

    private val generator1 = Generator.create { it.nextInt() }
    private val generator2 = Generator.create { it.nextDouble() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Unit) {
        checkForAll(generator1, generator2, iterations, seed) { _, _ -> invocation() }
    }

    @Test
    fun evaluateForRandomValues() {
        val valuesA = mutableSetOf<Int>()
        val valuesB = mutableSetOf<Double>()

        checkForAll(generator1, generator2, seed = 0L) { a, b ->
            valuesA += a
            valuesB += b
        }

        assertTrue(valuesA.size > 190)
        assertTrue(valuesB.size > 190)
    }

    @Test
    fun isPredictable() {
        val pass1 = mutableListOf<Pair<Int, Double>>()
        val pass2 = mutableListOf<Pair<Int, Double>>()

        val seed = 123564L

        checkForAll(generator1, generator2, seed = seed) { a, b ->
            pass1 += a to b
        }

        checkForAll(generator1, generator2, seed = seed) { a, b ->
            pass2 += a to b
        }

        assertEquals(pass1, pass2)
    }
}
