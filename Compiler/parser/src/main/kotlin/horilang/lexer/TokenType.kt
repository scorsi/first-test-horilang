package horilang.lexer

enum class TokenType {
    // KEYWORDS
    VAR,                    // VARIABLE DECLARATION
    VAL,                    // VALUE DECLARATION
    FUN,                    // FUNCTION DECLARATION
    IF,                     // IF
    ELSE,                   // ELSE

    MODULE,                 // MODULE
    IMPORT,                 // IMPORT
    EXPORT,                 // EXPORT

    // VALUES
    INTEGER,                // NUMBER
    FLOAT,                  // DECIMAL
    STRING,                 // STRING

    // USER-KEYWORD
    SYMBOL,                 // VARIABLE OR FUNCTION NAME
    BIG_SYMBOL,             // MODULE NAME OR METHOD/ATTRIBUTE

    // REGISTERED CHARACTERS
    DOT,                    // "."
    DDOT,                   // ":"
    LPAREN,                 // "("
    RPAREN,                 // ")"
    LBRACE,                 // "{"
    RBRACE,                 // "}"
    LBRACK,                 // "["
    RBRACK,                 // "]"
    LCHEV,                  // "<"
    RCHEV,                  // ">"
    COMMA,                  // ","
    LARROW,                 // "<-"
    RARROW,                 // "->"

    // ACTIONS
    ASSIGN,                 // ASSIGNMENT

    EQUAL,                  // IS EQUAL TO
    GREATER,                // IS GREATER TO
    LOWER,                  // IS LOWER TO
    GREATER_EQUAL,          // IS GREATER OR EQUAL TO
    LOWER_EQUAL,            // IS LOWER OR EQUAL TO

    SUB,                    // SUBSTRACTION
    ADD,                    // ADDITION
    MUL,                    // MULTIPLICATION
    DIV,                    // DIVISION
    MOD,                    // MODULO

    BINARY_AND,             // "&"
    BINARY_OR,              // "|"
    BINARY_XOR,             // "^"
    BINARY_NOT,             // "~"
    BINARY_LEFT_SHIFT,      // "<<"
    BINARY_RIGHT_SHIFT,     // ">>"

    LOGICAL_NOT,            // "!"
    LOGICAL_AND,            // "&&"
    LOGICAL_OR,             // "||"

    // END OF ....
    EOI,                    // END OF INSTRUCTION
    EOL,                    // END OF LINE : EOI EQUIVALENT
    EOF                     // END OF FILE
}