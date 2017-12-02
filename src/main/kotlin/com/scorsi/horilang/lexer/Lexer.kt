package com.scorsi.horilang.lexer

import com.scorsi.horilang.Token
import com.scorsi.horilang.TokenType

class Lexer constructor(val input: String, val rules: List<LexerRule>) {

    var stream: LexerStream = LexerStream(this)
    var tokens: MutableList<Token> = mutableListOf()
    var streamFinished: Boolean = false

    fun next(): Token =
            stream.next().let { value ->
                when (value) {
                    null -> {
                        streamFinished = true
                        Token(TokenType.EOF, "")
                    }
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

    fun peek(index: Int): Token? =
            if (tokens.size >= index) tokens[index - 1]
            else when (streamFinished) {
                true -> null
                else -> next().let { peek(index) }
            }

    fun purge(number: Int) = tokens.drop(number)

    fun clear() = tokens.clear()

}