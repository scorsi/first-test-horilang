package com.scorsi.horilang.lexer

enum class TokenType{
    // VAR STATEMENT
    VAR,

    // IF STATEMENT
    IF,
    ELSE,

    // PARENTHESIS
    LPAREN,
    RPAREN,

    // BRACKETS
    LBRACE,
    RBRACE,

    // ASSIGNMENT
    ASSIGN,

    // VARIABLE TYPE
    NUMBER,
    STRING,

    // SYMBOLS
    SYMBOL,
    TYPE,

    // QUOTES
    //DQUOTE,
    //SQUOTE,

    // SPECIALS
    SEMICOLON,
    DDOT,

    // COMPARISON OPERATORS
    EQUAL,
    NOTEQUAL,
    GREATER,
    LOWER,
    GREATEREQUAL,
    LOWEREQUAL,

    // CALCULATION OPERATORS
    MINUS,
    PLUS,
    MULTIPLICATION,
    DIVISION,
    MODULO;
}