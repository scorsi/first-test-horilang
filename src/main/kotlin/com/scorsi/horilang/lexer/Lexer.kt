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
                        val token = Token(TokenType.EOF, "")
                        println("Getting: $token")
                        token
                    }
                    else -> rules.first { value.matchesRule(it) }.let { rule ->
                        when (rule.type) {
                            TokenType.EOL -> Token(TokenType.EOI, "") // EOL BECOME EOI
                            else -> Token(rule.type, value)
                        }
                    }.let { token ->
                        println("Getting: $token")
                        tokens.add(token)
                        token
                    }
                }
            }

    fun peek(index: Int): Token? =
            if (tokens.size >= index) {
                println("Peeking: ${tokens[index - 1]} at $index")
                tokens[index - 1]
            } else when (streamFinished) {
                true -> null
                else -> next().let { peek(index) }
            }

    fun purge(number: Int): List<Token> =
            tokens.subList(0, number).let {
                val ret = it.toList()
                println("Purging: $it")
                for (i in 0 until number)
                    tokens.removeAt(0)
                println("Remaining: $tokens")
                ret
            }

    fun clear() = tokens.clear()

}