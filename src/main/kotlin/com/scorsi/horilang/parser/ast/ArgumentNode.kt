package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

data class ArgumentNode constructor(val symbol: SymbolNode, val type: Token) : Node()