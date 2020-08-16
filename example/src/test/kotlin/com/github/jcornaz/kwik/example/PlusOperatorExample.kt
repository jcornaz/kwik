package com.github.jcornaz.kwik.example

import com.github.jcornaz.kwik.evaluator.checkForAll
import com.github.jcornaz.kwik.evaluator.forAll
import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.combineWith
import com.github.jcornaz.kwik.generator.api.withNull
import com.github.jcornaz.kwik.generator.api.withSamples
import com.github.jcornaz.kwik.generator.stdlib.doubles
import com.github.jcornaz.kwik.generator.stdlib.enum
import com.github.jcornaz.kwik.generator.stdlib.ints
import com.github.jcornaz.kwik.generator.stdlib.strings
import com.github.jcornaz.kwik.generator.stdlib.withNaN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlusOperatorTest {

    //region Default config and generators
    @Test
    fun isCommutative() = forAll { x: Int, y: Int ->
        x + y == y + x
    }
    //endregion

    //region Using checkForAll
    @Test
    fun isCommutative2() = checkForAll { x: Int, y: Int ->
        assertEquals(x + y, y + x)
    }
    //endregion

    @Test
    fun isAssociative() {
        //region With a given number of iterations
        forAll(iterations = 1000) { x: Int, y: Int, z: Int ->
            (x + y) + z == x + (y + z)
        }
        //endregion
    }

    @Test
    fun zeroIsNeutral() {
        //region Use a seed to get reproducible results (useful when investigating a failure in the CI for instance)
        forAll(seed = -4567) { x: Int ->
            x + 0 == x
        }
        //endregion
    }

    @Test
    fun addNegativeSubtracts() {
        //region Configure or use a custom generator
        forAll(Generator.ints(min = 0), Generator.ints(max = -1)) { x, y ->
            x + y < x
        }
        //endregion
    }

    @Test
    fun skipEvaluation() {
        //region Skip evaluation
        forAll { x: Int, y: Int ->
            skipIf(x == y)

            x != y
        }
        //endregion
    }

    //region Create a custom generator
    val customGenerator1 = Generator { rng ->
        CustomClass(rng.nextInt(), rng.nextInt())
    }
    //endregion

    //region Create a generator for an enum
    val enumGenerator = Generator.enum<MyEnum>()

    val finiteValueGenerator = Generator.of("a", "b", "c")
    //endregion

    //region Combine generators
    val combinedGenerator = Generator.ints().combineWith(Generator.ints()) { x, y -> CustomClass(x, y) }
    //endregion

    //region Add samples
    val generator = Generator.ints().withSamples(13, 42)

    // since ``null`` and ``NaN`` are common edge-case, there are dedicated ``withNull`` and ``withNaN`` operators.
    val generatorWithNull = Generator.strings().withNull()
    val generatorWithNaN = Generator.doubles().withNaN()
    //endregion
}

data class CustomClass(val x: Int, val y: Int)
enum class MyEnum { A, B }
