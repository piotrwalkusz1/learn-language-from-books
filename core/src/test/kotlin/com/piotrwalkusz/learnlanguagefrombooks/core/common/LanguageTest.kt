package com.piotrwalkusz.learnlanguagefrombooks.core.common

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LanguageTest {

    @Test
    fun `convert upper case string to enum`() {
        val language = Language.valueOfIgnoreCase("ENGLISH")

        assertEquals(Language.ENGLISH, language)
    }

    @Test
    fun `convert lower case string to enum`() {
        val language = Language.valueOfIgnoreCase("english")

        assertEquals(Language.ENGLISH, language)
    }

    @Test
    fun `convert string with nonexistent language to enum`() {
        val language = Language.valueOfIgnoreCase("nonexistent_language")

        assertNull(language)
    }
}