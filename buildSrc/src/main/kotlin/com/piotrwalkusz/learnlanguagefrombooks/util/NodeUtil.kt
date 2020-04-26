package com.piotrwalkusz.learnlanguagefrombooks.util

import org.w3c.dom.Element
import org.w3c.dom.Node

fun Node.getChildrenByTagName(tagName: String): List<Element> {
    return childNodes
            .toList()
            .filterIsInstance<Element>()
            .filter { it.tagName == tagName }
}

fun Node.getFirstChildByTagName(tagName: String): Element? {
    return getChildrenByTagName(tagName).firstOrNull()
}