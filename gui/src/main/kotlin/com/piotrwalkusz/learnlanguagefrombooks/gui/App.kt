package com.piotrwalkusz.learnlanguagefrombooks.gui

import com.piotrwalkusz.learnlanguagefrombooks.core.TextProcessor
import com.piotrwalkusz.learnlanguagefrombooks.core.common.Language
import com.piotrwalkusz.learnlanguagefrombooks.core.dictionary.TranslationDictionary
import com.piotrwalkusz.learnlanguagefrombooks.core.filedecoder.PdfFileDecoder
import com.piotrwalkusz.learnlanguagefrombooks.core.wordcounter.LanguageToolWordCounter
import com.piotrwalkusz.learnlanguagefrombooks.core.wordexporter.CsvWordExporter
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class Application : App(FileLoaderView::class)

class FileLoaderView : View() {

    private var translationDictionary: File? = null
    private var knownWords: File? = null
    private var book: File? = null

    override val root = vbox {
        button("Translation dictionary") {
            action {
                translationDictionary = chooseFile(filters = arrayOf(FileChooser.ExtensionFilter("*", "*"))).firstOrNull()
            }
        }
        button("Known words") {
            action {
                knownWords = chooseFile(filters = arrayOf(FileChooser.ExtensionFilter("*", "*"))).firstOrNull()
            }
        }
        button("Book") {
            action {
                book = chooseFile(filters = arrayOf(FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"))).firstOrNull()
            }
        }
        button("Process") {
            action {
                val fileDecoder = PdfFileDecoder();
                val wordCounter = LanguageToolWordCounter(Language.ENGLISH)
                val dictionary = TranslationDictionary.createFromReader(translationDictionary!!.reader())
                val exporter = CsvWordExporter()

                val text = fileDecoder.read(book!!.inputStream())
                val words = wordCounter.countWords(text, dictionary.getLemmas()).keys
                val knownWordsList = knownWords!!.readLines()
                val unknownWords = words.minus(knownWordsList)
                        .map { dictionary.getRepresentationAndTranslatedForm(it) }
                        .map { it.representationForm to it.translatedForm }
                        .toMap()

                val output = File("flashcards.csv").writer()

                exporter.export(unknownWords).copyTo(output)

                output.close()

                if (words.minus(knownWordsList).isNotEmpty()) {
                    knownWords!!.appendText(words.minus(knownWordsList).joinToString("\n") + "\n")
                }
            }
        }
    }

}

class RootView : View() {

    override val root = vbox {
        val text = label { isWrapText = true }
        button("Press me") {
            action {
                val translationDictionary = TranslationDictionary.createFromReader(chooseFile(filters = emptyArray())[0].reader())
                val book = chooseFile(filters = emptyArray())[0].inputStream()

                val textProcessor = TextProcessor(PdfFileDecoder(), LanguageToolWordCounter(Language.ENGLISH), CsvWordExporter())

                text.text = textProcessor.translateAndExportWords(book, translationDictionary).readText()
            }
        }
    }
}




