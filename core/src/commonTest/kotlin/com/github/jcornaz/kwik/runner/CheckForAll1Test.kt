import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.checkForAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckForAll1Test : AbstractRunnerTest() {

    private val testGenerator = Generator.create { it.nextInt() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Unit) {
        checkForAll(testGenerator, iterations, seed) { invocation() }
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
