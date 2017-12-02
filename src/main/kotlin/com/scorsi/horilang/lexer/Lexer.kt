package com.scorsi.horilang.lexer

import com.scorsi.horilang.Token
import com.scorsi.horilang.TokenType

class Lexer constructor(val input: String, val rules: List<LexerRule>) {

    var stream: LexerStream = LexerStream(this)
    var tokens: MutableList<Token> = mutableListOf()

    fun next(): Token =
            stream.next().let { value ->
                when (value) {
                    null -> Token(TokenType.EOF, "")
                    else -> rules.first { value.matchesRule(it) }.let { rule ->
                        when (rule.type) {
                            TokenType.EOL -> Token(TokenType.EOI, "") // EOL BECOME EOI
                            else -> Token(rule.type, value)
                        }
                    }.let { token ->
                        tokens.add(token)
                        token
                    }
                }
            }

    fun clear() = tokens.clear()

}