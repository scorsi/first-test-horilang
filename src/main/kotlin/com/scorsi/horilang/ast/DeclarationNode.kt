package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class DeclarationNode : Node() {

    lateinit var symbol: String
    var value: String? = null
    var type: String? = null

    override fun build(tokens: List<Token>): Node {
        symbol = tokens[1].value
        if (tokens.size > 2) {
            value = tokens[3].value
            type = tokens[3].type.toString().toLowerCase().capitalize()
        }
        return this
    }

    override fun toString(): String {
        return "DeclarationNode(symbol=$symbol, type=$type, value=$value)"
    }
}