package horilang.parser

import horilang.lexer.Token

class ParserInstructionNotFound constructor(tokens: List<Token>)
    : Error("Instruction not found for theses tokens: ${tokens.joinToString()}")