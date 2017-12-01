package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

open class ValueNode constructor(val value: Token) : StatementNode() {
    override fun toString(): String {
        return "ValueNode(value=$value)"
    }
}