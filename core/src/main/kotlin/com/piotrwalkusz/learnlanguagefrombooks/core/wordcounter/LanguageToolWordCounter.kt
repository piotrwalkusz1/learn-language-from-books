package com.piotrwalkusz.learnlanguagefrombooks.core.wordcounter

import com.piotrwalkusz.learnlanguagefrombooks.core.common.Language
import org.languagetool.tagging.Tagger
import org.languagetool.tagging.en.EnglishTagger
import java.io.Reader

class LanguageToolWordCounter(language: Language) : WordCounter {

    private val tagger: Tagger = when (language) {
        Language.ENGLISH -> EnglishTagger()
        else -> throw IllegalArgumentException("LanguageToolLemmatizer does not support the $language language")
    }

    override fun countWords(source: Reader, dictionary: Set<String>): Map<String, Int> {
        return splitTextToWords(source)
                .mapNotNull { convertToWordFromDictionary(it, dictionary) }
                .groupingBy { it }
                .eachCount()
    }

    private fun splitTextToWords(source: Reader): List<String> {
        // TODO: splitting text to words removes sentence context
        return source.readText().split(Regex("\\P{L}"))
    }

    private fun convertToWordFromDictionary(word: String, dictionary: Set<String>): String? {
        val lemmas: List<String> = getLemmasOfWord(word)
        val lemmaInDictionary: String? = lemmas.find { dictionary.contains(it) }

        if (lemmaInDictionary != null) {
            return lemmaInDictionary
        }

        val lowerCaseWord: String = word.toLowerCase();
        if (dictionary.contains(lowerCaseWord)) {
            return lowerCaseWord
        }

        return null
    }

    private fun getLemmasOfWord(word: String): List<String> {
        return tagger.tag(listOf(word))[0].mapNotNull { it.lemma?.toLowerCase() }.distinct()
    }
}