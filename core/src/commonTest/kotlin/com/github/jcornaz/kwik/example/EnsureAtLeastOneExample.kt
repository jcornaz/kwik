package com.github.jcornaz.kwik.example

import com.github.jcornaz.kwik.evaluator.forAll
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test

class EnsureAtLeastOneExample {

    @Test
    fun ensureAtLeastOneEvaluation() {
        //region ensure at least one evaluation
        forAll { x: Int, y: Int ->

            // This forces the property to run as many times as necessary
            // so that we make sure to always test the case where x and y are both zero.
            ensureAtLeastOne("both are 0") { x == 0 && y == 0 }

            x * y == y * x
        }
        //endregion
    }

}

