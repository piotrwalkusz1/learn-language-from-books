package com.piotrwalkusz.learnlanguagefrombooks.util

import org.w3c.dom.Element

fun Element.getChildrenByTagName(tagName: String): List<Element> {
    return childNodes
            .toList()
            .filterIsInstance<Element>()
            .filter { it.tagName == tagName }
}

fun Element.getFirstChildByTagName(tagName: String): Element? {
    return getChildrenByTagName(tagName).firstOrNull()
}