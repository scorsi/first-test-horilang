package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class Expression : Node() {

    lateinit var leftValue: Node
    var operator: Node? = null
    var rightValue: Node? = null

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        nodes.lastOrNull().let {
            rightValue = when (it) {
                is Expression -> it
                is Value -> it
                else -> throw RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $it")
            }
        }
        nodes.removeAt(nodes.lastIndex)
        if (nodes.isEmpty()) { // Has just a value
            leftValue = rightValue!!
            rightValue = null
            return this
        }
        nodes.lastOrNull().let {
            operator = when (it) {
                is Operator -> it
                else -> throw RuntimeException("Unexpected parameter, wanted [Operator] but got $it")
            }
        }
        nodes.removeAt(nodes.lastIndex)
        nodes.lastOrNull().let {
            leftValue = when (it) {
                is Expression -> it
                is Value -> it
                else -> throw RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $it")
            }
        }
        nodes.removeAt(nodes.lastIndex)
        return this
    }

    override fun toString(): String = "Expression(leftValue=$leftValue, operator=$operator, rightValue=$rightValue)"

}