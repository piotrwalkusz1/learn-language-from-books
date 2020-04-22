package com.piotrwalkusz.learnlanguagefrombooks.core.dictionary

import com.piotrwalkusz.learnlanguagefrombooks.core.common.Language
import java.io.Reader
import java.io.Writer
import java.util.*

/*
    Basic terms:
    - lemma form - the lowercase lemma of word consisting of only letters, a user doesn't see this form,
                   only this form can be translated (e.g. hund; eat)
    - representation form - the form of a foreign language word that user sees (eg. der Hund; essen / aß / gegessen)
    - translated form - possible translation of words (e.g. dog; to eat, to dine)
    
    A Translation Dictionary file consists of the header line describing source and destination language:
    <source language>;<destination language>

    followed by lines in format:
    <lemma form>;<representation form>;<translated form>

    for example:
    german;polish
    hund;der Hund;dog
    essen;essen / aß / gegessen;to eat, to dine
*/

class TranslationDictionary private constructor(
        private val sourceLanguage: Language,
        private val destinationLanguage: Language,
        private val translations: SortedMap<String, RepresentationAndTranslatedForm>
) {

    data class RepresentationAndTranslatedForm(val representationForm: String, val translatedForm: String)

    companion object {

        fun createFromReader(reader: Reader): TranslationDictionary {
            val lines: List<String> = reader.readLines()
            require(lines.isNotEmpty()) { "Translation Dictionary file is empty" }
            val languagesLine: String = lines.first()
            val translationLines: List<String> = lines.drop(1)

            val languages: List<Language> = parseLanguageLine(languagesLine)
            val translations: SortedMap<String, RepresentationAndTranslatedForm> = parseTranslationLines(translationLines)

            return TranslationDictionary(languages[0], languages[1], translations)
        }

        private fun parseLanguageLine(line: String): List<Language> {
            return line.split(';').map { parseLanguage(it) }
        }

        private fun parseLanguage(language: String): Language {
            return Language.valueOfIgnoreCase(language)
                    ?: throw IllegalArgumentException("Language $language in Translation Dictionary file does not exist")
        }

        private fun parseTranslationLines(lines: List<String>): SortedMap<String, RepresentationAndTranslatedForm> {
            return lines.map { parseTranslationLine(it) }.toMap().toSortedMap()
        }

        private fun parseTranslationLine(line: String): Pair<String, RepresentationAndTranslatedForm> {
            val forms = line.split(';')
            require(forms.size == 3) { "Lines in Translation Dictionary has to be in format <lemma form>;<representation form>;<translated form>. Invalid line '$line' was founded." }
            return forms[0] to RepresentationAndTranslatedForm(forms[1], forms[2])
        }
    }

    fun write(writer: Writer) {
        writer.write("${sourceLanguage.name};${destinationLanguage.name}\n")
        for (translation in translations) {
            val lemma = translation.key
            val representation = translation.value.representationForm
            val translated = translation.value.translatedForm

            writer.write("$lemma;$representation;$translated\n")
        }
    }

    fun getLemmas(): Set<String> {
        return translations.keys
    }

    fun getRepresentation(lemma: String): String {
        return getRepresentationAndTranslatedForm(lemma).representationForm
    }

    fun translate(lemma: String): String {
        return getRepresentationAndTranslatedForm(lemma).translatedForm;
    }

    fun getRepresentationAndTranslatedForm(lemma: String): RepresentationAndTranslatedForm {
        return translations[lemma]
                ?: throw IllegalArgumentException("Word '$lemma' does not exist in Translation Dictionary")
    }
}