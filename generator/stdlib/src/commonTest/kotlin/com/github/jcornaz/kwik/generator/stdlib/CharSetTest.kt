package com.github.jcornaz.kwik.generator.stdlib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CharSetTest {

    @Test
    fun provideNumericCharacters() {
        assertEquals("0123456789".toSet(), CharSets.numeric)
    }

    @Test
    fun provideLowerCaseAlphabeticCharacters() {
        assertEquals("abcdefghijklmnopqrstuvwxyz".toSet(), CharSets.alphaLowerCase)
    }

    @Test
    fun provideUpperCaseAlphabeticCharacters() {
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toSet(), CharSets.alphaUpperCase)
    }

    @Test
    fun alphaContainsAllLowerCaseCharacters() {
        CharSets.alphaLowerCase.forEach {
            assertTrue(it in CharSets.alpha)
        }
    }

    @Test
    fun alphaContainsAllUpperCaseCharacters() {
        CharSets.alphaUpperCase.forEach {
            assertTrue(it in CharSets.alpha)
        }
    }

    @Test
    fun alphaDoesNotContainsCharactersOtherThanLowerAndUpperCaseAlpha() {
        (Char.MIN_VALUE..Char.MAX_VALUE).asSequence()
            .filterNot { it in CharSets.alphaLowerCase }
            .filterNot { it in CharSets.alphaUpperCase }
            .forEach {
                assertFalse(it in CharSets.alpha )
            }
    }

    @Test
    fun alphaNumContainsAllAlphaCharacters() {
        CharSets.alpha.forEach {
            assertTrue(it in CharSets.alphaNum)
        }
    }

    @Test
    fun alphaNumContainsAllNumericCharacters() {
        CharSets.numeric.forEach {
            assertTrue(it in CharSets.alphaNum)
        }
    }

    @Test
    fun alphaNulDoesNotContainsCharactersOtherThenAlphaAndNums() {
        (Char.MIN_VALUE..Char.MAX_VALUE).asSequence()
            .filterNot { it in CharSets.alpha }
            .filterNot { it in CharSets.numeric }
            .forEach {
                assertFalse(it in CharSets.alphaNum )
            }
    }

    @Test
    fun printableCharacterIsSuperSetOfAlphaNum() {
        CharSets.alphaNum.forEach {
            assertTrue(it in CharSets.printable)
        }
    }
}
