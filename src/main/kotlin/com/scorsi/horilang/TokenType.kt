package com.scorsi.horilang

enum class TokenType {
    // KEYWORDS
    VAR,                    // VARIABLE DECLARATION
    FUN,                    // FUNCTION DECLARATION

    // VALUES
    INTEGER,
    FLOAT,
    STRING,

    // USER-KEYWORD
    SYMBOL,                 // VARIABLE OR FUNCTION NAME

    // REGISTERED CHARACTERS
    DOT,                    // "."
    LPAREN,                 // "("
    RPAREN,                 // ")"
    LBRACE,                 // "{"
    RBRACE,                 // "}"

    // ACTIONS
    ASSIGN,                 // ASSIGNMENT

    EQUAL,                  // IS EQUAL TO
    GREATER,                // IS GREATER TO
    LOWER,                  // IS LOWER TO
    GREATEREQUAL,           // IS GREATER OR EQUAL TO
    LOWEREQUAL,             // IS LOWER OR EQUAL TO

    SUB,                    // SUBSTRACTION
    ADD,                    // ADDITION
    MUL,                    // MULTIPLICATION
    DIV,                    // DIVISION
    MOD,                    // MODULO

    // END OF ....
    EOI,                    // END OF INSTRUCTION
    EOL,                    // END OF LINE : EOI EQUIVALENT
    EOF                     // END OF FILE
}