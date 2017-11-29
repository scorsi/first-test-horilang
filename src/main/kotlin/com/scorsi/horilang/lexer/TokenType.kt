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

    SYMBOL,

    // QUOTES
    DQUOTE,
    SQUOTE,

    // END OF STATEMENT
    SEMICOLON,

    // COMPARISON OPERATORS
    EQUAL,

    // CALCULATION OPERATORS
    MINUS,
    PLUS,
    MULTIPLICATION,
    DIVISION,
    MODULO;
}