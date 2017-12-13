package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class AssignmentNode : Node() {

    lateinit var leftValue: String
    lateinit var rightValue: ValueNode

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        nodes.lastOrNull().let {
            when (it) {
                null -> throw Error()
                is ValueNode -> rightValue = it
                else -> throw Error()
            }
        }
        nodes.dropLast(1)

        leftValue = tokens[0].value

        return this
    }

    override fun toString(): String {
        return "AssignmentNode(leftValue=$leftValue, rightValue=$rightValue)"
    }
}