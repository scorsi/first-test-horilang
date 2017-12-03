package com.scorsi.horilang.lexer

import com.scorsi.horilang.Token
import com.scorsi.horilang.TokenType

class Lexer constructor(val input: String, val rules: List<LexerRule>) {

    var tokens: MutableList<Token> = mutableListOf()
    var streamFinished: Boolean = false

    private var startPos = 0
    private var line = 0
    private var column = 0
    private var columnToRemove = 0
    private var lastColumn = 0

    private fun skipWhitespace(pos: Int): Int =
            when {
                pos >= input.length -> pos
                input[pos].toString().matches("""[ \t]""".toRegex()) -> skipWhitespace(pos + 1)
                else -> pos
            }

    private fun advanceToNextToken(endPos: Int, matched: Boolean = false): Int =
            try {
                when {
                    endPos == startPos -> when {
                        endPos >= input.length -> input.length + 1
                        rules.any { input[endPos].toString().matchesRule(it) } ->
                            advanceToNextToken(endPos + 1, true)
                        else -> when (matched) {
                            true -> endPos - 1
                            false -> advanceToNextToken(endPos + 1, matched)
                        }
                    }
                    endPos > startPos -> when {
                        endPos > input.length -> when (matched) {
                            true -> endPos - 1
                            false -> throw LexerSyntaxError(input.substring(startPos, endPos))
                        }
                        rules.any { input.substring(startPos, endPos).matchesRule(it) } ->
                            advanceToNextToken(endPos + 1, true)
                        else -> when (matched) {
                            true -> endPos - 1
                            false -> advanceToNextToken(endPos + 1, matched)
                        }
                    }
                    else -> throw LexerUnknownError()
                }
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
                throw LexerUnknownError()
            }

    private fun readUntilNextToken(): String? =
            when {
                startPos >= input.length -> null
                else -> skipWhitespace(startPos).let {
                    startPos = it
                    advanceToNextToken(startPos).let { endPos ->
                        when {
                            endPos > input.length || endPos < startPos -> null
                            endPos == startPos -> input[endPos].toString()
                            else -> input.substring(startPos, endPos)
                        }.let { ret ->
                            column = endPos - columnToRemove + 1
                            startPos = endPos
                            ret
                        }
                    }
                }
            }

    fun nextToken(): Token =
            readUntilNextToken().let { value ->
                when (value) {
                    null -> {
                        streamFinished = true
                        val token = Token(TokenType.EOF, "", line, lastColumn)
                        println("Getting: $token")
                        token
                    }
                    else -> rules.first { value.matchesRule(it) }.let { rule ->
                        when (rule.type) {
                            TokenType.EOL -> {
                                line++
                                columnToRemove += column
                                column = 0
                                Token(TokenType.EOI, "\n", line - 1, lastColumn) // EOL BECOME EOI
                            }
                            else -> Token(rule.type, value, line, lastColumn)
                        }
                    }.let { token ->
                        lastColumn = column
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
                else -> nextToken().let { peek(index) }
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