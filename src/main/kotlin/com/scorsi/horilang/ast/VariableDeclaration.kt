package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class VariableDeclaration : Node() {

    lateinit var symbol: String
    var rightValue: Node? = null

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        this.symbol = tokens[1].value
        if (tokens.size > 2) {
            nodes.lastOrNull().let {
                this.rightValue = when (it) {
                    is Value -> it
                    is Expression -> it
                    else -> throw RuntimeException("Unexpected parameter, wanted [Value, Expression] but got $it")
                }
            }
            nodes.removeAt(nodes.lastIndex)
        }
        return this
    }

    override fun toString(): String = "VariableDeclaration(symbol=$symbol, rightValue=$rightValue)"

}