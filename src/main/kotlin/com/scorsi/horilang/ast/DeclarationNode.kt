package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class DeclarationNode : Node() {

    lateinit var symbol: String
    var rightValue: ValueNode? = null

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        symbol = tokens[1].value
        if (tokens.size > 2) {
            nodes.lastOrNull().let {
                when (it) {
                    null -> throw Error()
                    is ValueNode -> rightValue = it
                    else -> throw Error()
                }
            }
            nodes.dropLast(1)
        }
        return this
    }

    override fun toString(): String {
        return "DeclarationNode(symbol=$symbol, rightValue=$rightValue)"
    }
}