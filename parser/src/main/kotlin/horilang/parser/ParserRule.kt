package horilang.parser

import horilang.lexer.TokenType

data class ParserRule @JvmOverloads constructor(var token: TokenType? = null, var specialRule: String? = null, val isEnd: Boolean = false)