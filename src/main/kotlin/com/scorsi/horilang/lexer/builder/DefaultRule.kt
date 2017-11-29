package com.scorsi.horilang.lexer.builder

import com.scorsi.horilang.lexer.Rule
import com.scorsi.horilang.lexer.TokenType

enum class DefaultRule constructor(val tokenType: TokenType, val regex: Regex) {
    VAR(TokenType.VAR, """var""".toRegex()),
    IF(TokenType.IF, """if""".toRegex()),
    ELSE(TokenType.ELSE, """else""".toRegex()),
    STRING(TokenType.STRING, """\".*\"""".toRegex()),
    SYMBOL(TokenType.SYMBOL, """[a-zA-Z_][a-zA-Z0-9_]*""".toRegex()), // NAME RULE HAS NOT PRIORITY OVER OTHER KEYWORDS RULE
    LPAREN(TokenType.LPAREN, """\(""".toRegex()),
    RPAREN(TokenType.RPAREN, """\)""".toRegex()),
    LBRACE(TokenType.LBRACE, """\{""".toRegex()),
    RBRACE(TokenType.RBRACE, """\}""".toRegex()),
    EQUAL(TokenType.EQUAL, """==""".toRegex()), // EQUAL RULE HAS PRIORITY OVER ASSIGN RULE
    ASSIGN(TokenType.ASSIGN, """=""".toRegex()),
    NUMBER(TokenType.NUMBER, """[0-9]+""".toRegex()),
    DQUOTE(TokenType.DQUOTE, """\"""".toRegex()),
    SQUOTE(TokenType.SQUOTE, """\'""".toRegex()),
    SEMICOLON(TokenType.SEMICOLON, """;""".toRegex()),
    MINUS(TokenType.MINUS, """-""".toRegex()),
    PLUS(TokenType.PLUS, """\+""".toRegex()),
    MULTIPLICATION(TokenType.MULTIPLICATION, """\*""".toRegex()),
    DIVISION(TokenType.DIVISION, """/""".toRegex()),
    MODULO(TokenType.MODULO, """%""".toRegex()),;

    fun build() = Rule(regex, tokenType)
}