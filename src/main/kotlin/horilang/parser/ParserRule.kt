package horilang.parser

import horilang.TokenType

data class ParserRule @JvmOverloads constructor(var token: TokenType? = null, var specialRule: String? = null)