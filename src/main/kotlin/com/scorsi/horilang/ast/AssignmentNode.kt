package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class AssignmentNode : Node() {

    lateinit var leftValue: String
    lateinit var rightValue: String
    lateinit var rightValueType: String

    override fun build(tokens: List<Token>): Node {
        leftValue = tokens[0].value
        rightValue = tokens[2].value
        rightValueType = tokens[2].type.toString().toLowerCase().capitalize()

        return this
    }

    override fun toString(): String {
        return "AssignmentNode(leftValue=$leftValue, rightValue=$rightValue, rightValueType=$rightValueType)"
    }
}