package horilang.lexer

data class Token @JvmOverloads constructor(val type: TokenType, val value: String, val line: Int = 0, val column: Int = 0)