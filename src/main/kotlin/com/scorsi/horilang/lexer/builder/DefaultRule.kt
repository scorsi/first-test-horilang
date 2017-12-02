package com.scorsi.horilang.lexer.builder

import com.scorsi.horilang.lexer.Rule
import com.scorsi.horilang.lexer.TokenType

enum class DefaultRule constructor(val tokenType: TokenType, val regex: Regex) {
    VAR(TokenType.VAR, """var""".toRegex()),
    FUNC(TokenType.FUNC, """func""".toRegex()),
    IF(TokenType.IF, """if""".toRegex()),
    ELSE(TokenType.ELSE, """else""".toRegex()),
    STRING(TokenType.STRING, """\".*\"""".toRegex()),
    TYPE(TokenType.TYPE, """[A-Z][a-zA-Z_]*""".toRegex()),
    SYMBOL(TokenType.SYMBOL, """_?[a-z][a-zA-Z0-9]*""".toRegex()), // NAME RULE HAS NOT PRIORITY OVER OTHER KEYWORDS RULE
    LPAREN(TokenType.LPAREN, """\(""".toRegex()),
    RPAREN(TokenType.RPAREN, """\)""".toRegex()),
    LBRACE(TokenType.LBRACE, """\{""".toRegex()),
    RBRACE(TokenType.RBRACE, """\}""".toRegex()),
    EQUAL(TokenType.EQUAL, """==""".toRegex()), // EQUAL RULE HAS PRIORITY OVER ASSIGN RULE
    NOTEQUAL(TokenType.NOTEQUAL, """!=""".toRegex()),
    GREATER(TokenType.GREATER, """>""".toRegex()),
    GREATEREQUAL(TokenType.GREATEREQUAL, """>=""".toRegex()),
    LOWER(TokenType.LOWER, """<""".toRegex()),
    LOWEREQUAL(TokenType.LOWEREQUAL, """<=""".toRegex()),

    ASSIGN(TokenType.ASSIGN, """=""".toRegex()),
    NUMBER(TokenType.NUMBER, """[0-9]+""".toRegex()),
    //DQUOTE(TokenType.DQUOTE, """\"""".toRegex()),
    //SQUOTE(TokenType.SQUOTE, """\'""".toRegex()),
    DDOT(TokenType.DDOT, """:""".toRegex()),
    SEMICOLON(TokenType.SEMICOLON, """;""".toRegex()),
    MINUS(TokenType.MINUS, """-""".toRegex()),
    PLUS(TokenType.PLUS, """\+""".toRegex()),
    MULTIPLICATION(TokenType.MULTIPLICATION, """\*""".toRegex()),
    DIVISION(TokenType.DIVISION, """/""".toRegex()),
    MODULO(TokenType.MODULO, """%""".toRegex()),;

    fun build() = Rule(regex, tokenType)
}