package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

class Block : Node() {

    lateinit var statements: List<Node>

    override fun build(tokens: List<Token>, nodes: MutableList<Node>): Node {
        statements = nodes.toList()
        nodes.clear()
        return this
    }

    override fun toString(): String = "Block(statements=$statements)"

}