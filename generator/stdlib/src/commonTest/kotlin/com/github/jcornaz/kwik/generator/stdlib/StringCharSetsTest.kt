package com.github.jcornaz.kwik.generator.stdlib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("DEPRECATION")
class StringCharSetsTest {

    @Test
    fun provideNumericCharacters() {
        assertEquals("0123456789".toSet(), StringCharSets.numeric)
    }

    @Test
    fun provideLowerCaseAlphabeticCharacters() {
        assertEquals("abcdefghijklmnopqrstuvwxyz".toSet(), StringCharSets.alphaLowerCase)
    }

    @Test
    fun provideUpperCaseAlphabeticCharacters() {
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toSet(), StringCharSets.alphaUpperCase)
    }

    @Test
    fun alphaContainsAllLowerCaseCharacters() {
        StringCharSets.alphaLowerCase.forEach {
            assertTrue(it in StringCharSets.alpha)
        }
    }

    @Test
    fun alphaContainsAllUpperCaseCharacters() {
        StringCharSets.alphaUpperCase.forEach {
            assertTrue(it in StringCharSets.alpha)
        }
    }

    @Test
    fun alphaDoesNotContainsCharactersOtherThanLowerAndUpperCaseAlpha() {
        (Char.MIN_VALUE..Char.MAX_VALUE).asSequence()
            .filterNot { it in StringCharSets.alphaLowerCase }
            .filterNot { it in StringCharSets.alphaUpperCase }
            .forEach {
                assertFalse(it in StringCharSets.alpha )
            }
    }

    @Test
    fun alphaNumContainsAllAlphaCharacters() {
        StringCharSets.alpha.forEach {
            assertTrue(it in StringCharSets.alphaNum)
        }
    }

    @Test
    fun alphaNumContainsAllNumericCharacters() {
        StringCharSets.numeric.forEach {
            assertTrue(it in StringCharSets.alphaNum)
        }
    }

    @Test
    fun alphaNulDoesNotContainsCharactersOtherThenAlphaAndNums() {
        (Char.MIN_VALUE..Char.MAX_VALUE).asSequence()
            .filterNot { it in StringCharSets.alpha }
            .filterNot { it in StringCharSets.numeric }
            .forEach {
                assertFalse(it in StringCharSets.alphaNum )
            }
    }

    @Test
    fun printableCharacterIsSuperSetOfAlphaNum() {
        StringCharSets.alphaNum.forEach {
            assertTrue(it in StringCharSets.printable)
        }
    }
}
