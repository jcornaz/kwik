package com.github.jcornaz.kwik.example

import com.github.jcornaz.kwik.evaluator.forAll
import com.github.jcornaz.kwik.generator.api.Generator
import kotlin.test.Test

class EnsureAtLeastOneTest {

    @Test
    fun ensureAtLeastOneEvaluation() {
        //region ensure at least one evaluation
        forAll<Int>(
            Generator.create { 42 }
        ) { x ->
            ensureAtLeastOne { x > 10 }
            true
        }
        //endregion
    }

}

