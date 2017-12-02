package com.scorsi.horilang.lexer

import com.scorsi.horilang.TokenType

data class LexerRule constructor(val type: TokenType, val match: String)