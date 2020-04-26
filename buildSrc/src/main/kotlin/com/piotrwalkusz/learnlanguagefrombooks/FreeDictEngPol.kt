package com.piotrwalkusz.learnlanguagefrombooks

import com.piotrwalkusz.learnlanguagefrombooks.util.getChildrenByTagName
import com.piotrwalkusz.learnlanguagefrombooks.util.getFirstChildByTagName
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class FreeDictEngPol {

    data class Word(
            val basicForm: String,
            val translations: List<Translation>
    )

    data class Translation(
            val value: String,
            val position: String?
    )

    fun parse(file: File): String {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val wordNodes = document.getFirstChildByTagName("div")?.getChildrenByTagName("entry")
                ?: throw IllegalStateException("No div node in root node")
        val words = parseWordNodes(wordNodes)

        return words.joinToString("\n") { convertWordToDictionaryEntry(it) }
    }

    private fun convertWordToDictionaryEntry(word: Word): String {
        val translations = word.translations.joinToString { it.value }

        return "${word.basicForm.toLowerCase()};${word.basicForm};$translations"
    }

    private fun parseWordNodes(wordNodes: List<Element>): List<Word> {
        return wordNodes.map { parseWordNode(it) }
    }

    private fun parseWordNode(wordNode: Element): Word {
        return Word(
                parseBasicFormFromWordNode(wordNode),
                parseTranslationsFromWordNode(wordNode)
        )
    }

    private fun parseBasicFormFromWordNode(wordNode: Element): String {
        return wordNode
                .getFirstChildByTagName("form")
                ?.getFirstChildByTagName("orth")
                ?.textContent
                ?: throw IllegalStateException("Cannot parse basic form")
    }

    private fun parseTranslationsFromWordNode(wordNode: Element): List<Translation> {
        return wordNode.getChildrenByTagName("sense")
                .flatMap { parseTranslationsFromSenseNode(it) }
    }

    private fun parseTranslationsFromSenseNode(senseNode: Element): List<Translation> {
        val position = parsePositionFromSenseNode(senseNode)

        return senseNode
                .getChildrenByTagName("sense")
                .flatMap { it.getChildrenByTagName("sense") }
                .flatMap { it.getChildrenByTagName("cit") }
                .filter { it.getAttribute("type") == "trans" }
                .mapNotNull { it.getFirstChildByTagName("quote") }
                .map { it.textContent.trim() }
                .map { Translation(it, position) }
    }

    private fun parsePositionFromSenseNode(senseNode: Element): String? {
        return senseNode
                .getFirstChildByTagName("gramGrp")
                ?.getFirstChildByTagName("pos")
                ?.textContent
    }
}
