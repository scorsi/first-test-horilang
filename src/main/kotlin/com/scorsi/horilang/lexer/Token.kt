package com.scorsi.horilang.lexer

data class Token constructor(val type: TokenType, val value: String? = null)