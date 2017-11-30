package com.scorsi.horilang.lexer

data class Token constructor(val type: TokenType, val value: Any? = null)