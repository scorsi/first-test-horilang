package com.scorsi.horilang.lexer

class LexerStream constructor(private val lexer: Lexer) {

    private var startPos = 0
    var line = 0
    var column = 0

    private fun skipWhitespace(pos: Int): Int =
            when {
                pos >= lexer.input.length -> pos
                lexer.input[pos].toString().matches("""[ \t]""".toRegex()) -> skipWhitespace(pos + 1)
                else -> pos
            }

    private fun advanceToNextToken(endPos: Int, matched: Boolean = false): Int =
            try {
                when {
                    endPos == startPos -> when {
                        endPos >= lexer.input.length -> when (matched) {
                            true -> endPos - 1
                            false -> throw LexerSyntaxError(lexer.input[endPos].toString())
                        }
                        lexer.rules.any { lexer.input[endPos].toString().matchesRule(it) } ->
                            advanceToNextToken(endPos + 1, true)
                        else -> when (matched) {
                            true -> endPos - 1
                            false -> advanceToNextToken(endPos + 1, matched)
                        }
                    }
                    endPos > startPos -> when {
                        endPos > lexer.input.length -> when (matched) {
                            true -> endPos - 1
                            false -> throw LexerSyntaxError(lexer.input.substring(startPos, endPos))
                        }
                        lexer.rules.any { lexer.input.substring(startPos, endPos).matchesRule(it) } ->
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

    fun next(): String? =
            when {
                startPos >= lexer.input.length -> null
                else -> skipWhitespace(startPos).let {
                    startPos = it
                    advanceToNextToken(startPos).let { endPos ->
                        when {
                            endPos > lexer.input.length || endPos < startPos -> null
                            endPos == startPos -> lexer.input[endPos].toString()
                            else -> lexer.input.substring(startPos, endPos)
                        }.let { ret ->
                            line = endPos - startPos
                            startPos = endPos
                            ret
                        }
                    }
                }
            }

}