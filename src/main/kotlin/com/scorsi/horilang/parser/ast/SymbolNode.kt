package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.TokenType

class SymbolNode constructor(value: String) : ValueNode(value, TokenType.SYMBOL) {
    override fun toString(): String {
        return "SymbolNode(value=$value)"
    }
}