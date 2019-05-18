import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.forAll
import com.github.jcornaz.kwik.runner.AbstractRunnerTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ForAll1Test : AbstractRunnerTest() {

    private val testGenerator = Generator.create { it.nextInt() }

    override fun evaluate(iterations: Int, seed: Long, invocation: () -> Unit) {
        forAll(testGenerator, iterations, seed) { invocation(); true }
    }

    @Test
    fun failFastInCaseOfFalsification() {
        var invocations = 0

        assertFailsWith<AssertionError> {
            forAll(testGenerator, iterations = 200) { _ ->
                invocations++
                false
            }
        }

        assertEquals(1, invocations)
    }

    @Test
    fun evaluateForRandomValues() {
        val values = mutableSetOf<Int>()

        forAll(testGenerator, seed = 0L) { it ->
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

        forAll(gen, seed = seed) { it ->
            pass1 += it
            true
        }

        forAll(gen, seed = seed) { it ->
            pass2 += it
            true
        }

        assertEquals(pass1, pass2)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun canBeCalledWithoutExplicitGenerator() {
        forAll { it: Int -> it is Int }
    }
}

