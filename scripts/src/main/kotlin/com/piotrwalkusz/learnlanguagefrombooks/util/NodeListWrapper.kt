package com.piotrwalkusz.learnlanguagefrombooks.util

import org.w3c.dom.Node
import org.w3c.dom.NodeList

class NodeListWrapper(private val nodeList: NodeList) : AbstractList<Node>(), RandomAccess {

    override val size: Int
        get() = nodeList.length

    override fun get(index: Int): Node {
        return nodeList.item(index)
    }
}

fun NodeList.toList(): NodeListWrapper {
    return NodeListWrapper(this)
}