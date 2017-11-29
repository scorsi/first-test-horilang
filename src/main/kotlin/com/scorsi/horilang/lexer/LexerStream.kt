package com.scorsi.horilang.lexer

class LexerStream constructor(val rules: List<Rule>, private var input: String) {

    var startPos: Int = 0

    init {
        input += " "
    }

    private fun String.matchRule(rule: Rule) = matches(rule.regex)

    private fun skipWhitespace(pos: Int): Int =
            when {
                pos >= input.length -> pos
                input[pos].toString().matches("""\s""".toRegex()) -> skipWhitespace(pos + 1)
                else -> pos
            }

    private fun advance(pos: Int): Int =
            when {
                pos >= input.length -> pos - 1
                else -> when (pos) {
                    startPos -> when {
                        rules.any { input[pos].toString().matchRule(it) } -> advance(pos + 1)
                        else -> pos - 1
                    }
                    else -> when {
                        rules.any { input.substring(startPos, pos).matchRule(it) } -> advance(pos + 1)
                        else -> pos - 1
                    }
                }
            }

    private fun createToken(s: String): Token = rules.first { s.matchRule(it) }.let { Token(it.tokenType, s) }

    fun next(): Token? =
            when {
                startPos >= input.length -> null
                else -> skipWhitespace(startPos).let { pos ->
                    startPos = pos
                    advance(startPos).let { endPos ->
                        when {
                            endPos >= input.length || endPos < startPos -> null
                            endPos == startPos -> input[endPos].toString()
                            else -> input.substring(startPos, endPos)
                        }.let { ret ->
                            startPos = endPos
                            when (ret) {
                                null -> null
                                else -> createToken(ret)
                            }
                        }
                    }
                }
            }

}