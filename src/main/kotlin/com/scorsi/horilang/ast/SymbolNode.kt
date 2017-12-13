package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class SymbolNode : Node() {

    lateinit var symbol: String

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        symbol = tokens[0].value

        return this
    }

    override fun toString(): String {
        return "SymbolNode(symbol=$symbol)"
    }
}