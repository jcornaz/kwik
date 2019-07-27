package com.github.jcornaz.kwik.generator.stdlib

import kotlin.test.Test
import kotlin.test.assertTrue

class StringCharSetsSpec {

    @Test
    fun alphaCharsetContainsAllAlphabeticCharacters() {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        alphabet.forEach {
            assertTrue(it in StringCharSets.alphabetic)
        }
    }

    @Test
    fun alphaCharsetContainsDoesNotContainNonAlphabeticCharacters() {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        StringCharSets.alphabetic.forEach {
            assertTrue(it in alphabet)
        }
    }

    @Test
    fun numericCharsetContainsAllNumericCharacters() {
        "1234567890".forEach {
            assertTrue(it in StringCharSets.numeric)
        }
    }

    @Test
    fun numericCharsetDoesNotContainNonNumericCharacters() {
        StringCharSets.numeric.forEach {
            assertTrue(it in "1234567890")
        }
    }
}
