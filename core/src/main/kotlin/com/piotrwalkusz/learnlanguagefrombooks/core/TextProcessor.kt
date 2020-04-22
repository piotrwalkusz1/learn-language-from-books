package com.piotrwalkusz.learnlanguagefrombooks.core

import com.piotrwalkusz.learnlanguagefrombooks.core.dictionary.TranslationDictionary
import com.piotrwalkusz.learnlanguagefrombooks.core.filedecoder.FileDecoder
import com.piotrwalkusz.learnlanguagefrombooks.core.wordcounter.WordCounter
import com.piotrwalkusz.learnlanguagefrombooks.core.wordexporter.WordExporter
import java.io.InputStream
import java.io.Reader

class TextProcessor(
        private val fileDecoder: FileDecoder,
        private val wordCounter: WordCounter,
        private val wordExporter: WordExporter
) {

    data class TranslationWithCount(val word: String, val count: Int)

    fun translateAndExportWords(source: InputStream, dictionary: TranslationDictionary): Reader {
        val translations: Map<String, String> = translateWords(source, dictionary)

        return wordExporter.export(translations)
    }

    fun countAndTranslateWords(text: InputStream, dictionary: TranslationDictionary): Map<String, TranslationWithCount> {
        return countWords(text, dictionary.getLemmas())
                .map { (lemma, count) -> lemma to TranslationWithCount(dictionary.translate(lemma), count) }
                .toMap()
    }

    fun translateWords(source: InputStream, dictionary: TranslationDictionary): Map<String, String> {
        return countWords(source, dictionary.getLemmas())
                .keys
                .map { dictionary.getRepresentationAndTranslatedForm(it) }
                .map { it.representationForm to it.translatedForm }
                .toMap()
    }

    fun countWords(source: InputStream, dictionary: Set<String>): Map<String, Int> {
        val text: Reader = fileDecoder.read(source)

        return wordCounter.countWords(text, dictionary)
    }
}