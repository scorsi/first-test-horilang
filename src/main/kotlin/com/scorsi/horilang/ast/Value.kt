package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class Value : Node() {

    lateinit var value: String
    lateinit var type: String

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        this.value = tokens[0].value
        this.type = tokens[0].type.toString().toLowerCase().capitalize()
        return this
    }

    override fun toString(): String = "Value(value=$value, type=$type)"

}