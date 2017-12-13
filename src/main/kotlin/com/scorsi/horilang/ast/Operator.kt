package com.scorsi.horilang.ast

import com.scorsi.horilang.Token
import com.scorsi.horilang.TokenType

class Operator : Node() {

    lateinit var value: TokenType

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        value = tokens[0].type
        return this
    }

    override fun toString(): String = "Operator(value=$value)"

}