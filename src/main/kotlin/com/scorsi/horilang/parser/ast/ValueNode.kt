package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.TokenType

open class ValueNode constructor(val value: String, val type: TokenType) : StatementNode() {
    override fun toString(): String {
        return "ValueNode(value=$value)"
    }
}