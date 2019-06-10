package com.github.jcornaz.kwik.example

import com.github.jcornaz.kwik.*
import com.github.jcornaz.kwik.generator.doubles
import com.github.jcornaz.kwik.generator.enum
import com.github.jcornaz.kwik.generator.ints
import com.github.jcornaz.kwik.generator.withNaN
import kotlin.test.Test

class PlusOperatorTest {

    //region Default config and generators
    @Test
    fun isCommutative() = forAll { x: Int, y: Int ->
        x + y == y + x
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
    val customGenerator1 = Generator.create { rng -> CustomClass(rng.nextInt(), rng.nextInt()) }
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

    val generatorWithNull = Generator.ints().withNull()
    val generatorWithNaN = Generator.doubles().withNaN()
    //endregion
}

data class CustomClass(val x: Int, val y: Int)
enum class MyEnum { A, B }
