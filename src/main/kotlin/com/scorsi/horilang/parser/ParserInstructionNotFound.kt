package com.scorsi.horilang.parser

import com.scorsi.horilang.Token

class ParserInstructionNotFound constructor(tokens: List<Token>)
    : Error("Instruction not found for theses tokens: ${tokens.joinToString()}")