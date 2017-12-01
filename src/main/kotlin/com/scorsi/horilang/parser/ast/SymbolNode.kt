package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

class SymbolNode constructor(value: Token) : ValueNode(value) {
    override fun toString(): String {
        return return "SymbolNode(value=$value)"
    }
}