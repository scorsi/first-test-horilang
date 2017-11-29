package com.scorsi.horilang.lexer

class Lexer constructor(val rules: List<Rule>) {

    fun lex(input: String) = LexerStream(rules, input)

}