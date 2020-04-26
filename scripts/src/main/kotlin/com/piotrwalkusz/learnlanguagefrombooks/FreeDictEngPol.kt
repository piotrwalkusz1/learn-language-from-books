package com.piotrwalkusz.learnlanguagefrombooks

import com.piotrwalkusz.learnlanguagefrombooks.util.NodeListWrapper
import com.piotrwalkusz.learnlanguagefrombooks.util.getChildrenByTagName
import com.piotrwalkusz.learnlanguagefrombooks.util.getFirstChildByTagName
import com.piotrwalkusz.learnlanguagefrombooks.util.toList
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    FreeDictEngPol().parse("/home/piotr/Projects/learn-language-from-books/scripts/a.xml")
}

class FreeDictEngPol {

    data class Word(
            val basicForm: String,
            val translations: List<Translation>
    )

    data class Translation(
            val value: String,
            val position: String?
    )

    fun parse(path: String) {
        val file = File(path)
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

        val wordNodes = document.getElementsByTagName("div").item(0).childNodes

        parseWordNodes(wordNodes).forEach {
            println(it.basicForm + " - " + it.translations.joinToString { tran -> tran.value + " (" + tran.position + ")" })
        }
    }

    private fun parseWordNodes(wordNodes: NodeList): List<Word> {
        return NodeListWrapper(wordNodes)
                .filterIsInstance<Element>()
                .map { parseWordNode(it) }
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
