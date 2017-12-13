package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class VariableAssignment : Node() {

    lateinit var leftValue: String
    lateinit var rightValue: Node

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        nodes.lastOrNull().let {
            this.rightValue = when (it) {
                is Value -> it
                is Expression -> it
                else -> throw RuntimeException("Unexpected parameter, wanted [Value, Expression] but got $it")
            }
        }
        nodes.removeAt(nodes.lastIndex)
        this.leftValue = tokens[0].value
        return this
    }

    override fun toString(): String = "VariableAssignment(leftValue=$leftValue, rightValue=$rightValue)"

}