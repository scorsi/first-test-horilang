package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class DeclarationNode : Node() {

    lateinit var symbol: String
    var rightValue: ValueNode? = null

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        this.symbol = tokens[1].value
        if (tokens.size > 2) {
            nodes.lastOrNull().let {
                when (it) {
                    is ValueNode -> this.rightValue = it
                    else -> throw Error()
                }
            }
            nodes.dropLast(1)
        }
        return this
    }

    override fun toString(): String = "DeclarationNode(symbol=$symbol, rightValue=$rightValue)"
}