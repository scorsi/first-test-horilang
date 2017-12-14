package horilang.lexer

import horilang.TokenType

data class LexerRule constructor(val type: TokenType, val match: String)