import com.github.jcornaz.kwik.Generator
import com.github.jcornaz.kwik.combineWith
import com.github.jcornaz.kwik.forAll
import com.github.jcornaz.kwik.generator.ints
import kotlin.test.Test

class PlusOperatorTest {

    // Default config and generators
    @Test
    fun isCommutative() = forAll { x: Int, y: Int ->
        x + y == y + x
    }

    // With a given number of iterations
    @Test
    fun isAssociative() = forAll(iterations = 1000) { x: Int, y: Int, z: Int ->
        (x + y) + z == x + (y + z)
    }

    // Use a seed to get reproducible results (useful when investigating a failure in the CI for instance)
    @Test
    fun zeroIsNeutral() = forAll(seed = -4567) { x: Int ->
        x + 0 == x
    }

    // Configure or use a custom generator
    @Test
    fun addNegativeSubtracts() = forAll(Generator.ints(min = 0), Generator.ints(max = -1)) { x, y ->
        x + y < x
    }

    // Create a custom generator
    val customGenerator1 = Generator.create { rng -> CustomClass(rng.nextInt(), rng.nextInt()) }

    // Combine generators
    val customGenerator2 = Generator.ints().combineWith(Generator.ints()) { x, y -> CustomClass(x, y) }
}

data class CustomClass(val x: Int, val y: Int)
