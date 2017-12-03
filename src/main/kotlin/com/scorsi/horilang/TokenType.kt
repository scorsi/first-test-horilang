package com.scorsi.horilang

enum class TokenType {
    // KEYWORDS
    VAR,                    // VARIABLE DECLARATION
    FUN,                    // FUNCTION DECLARATION
    IF,                     // IF
    ELSE,                   // ELSE

    // VALUES
    INTEGER,                // NUMBER
    FLOAT,                  // DECIMAL
    STRING,                 // STRING

    // USER-KEYWORD
    SYMBOL,                 // VARIABLE OR FUNCTION NAME

    // REGISTERED CHARACTERS
    DOT,                    // "."
    DDOT,                   // ":"
    LPAREN,                 // "("
    RPAREN,                 // ")"
    LBRACE,                 // "{"
    RBRACE,                 // "}"
    COMMA,                  // ","

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