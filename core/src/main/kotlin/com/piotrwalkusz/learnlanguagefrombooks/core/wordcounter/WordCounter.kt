package com.piotrwalkusz.learnlanguagefrombooks.core.wordcounter

import java.io.Reader

interface WordCounter {

    fun countWords(source: Reader, dictionary: Set<String>): Map<String, Int>
}