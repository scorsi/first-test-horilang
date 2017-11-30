package com.scorsi.horilang.parser

import com.scorsi.horilang.lexer.TokenType
import com.scorsi.horilang.lexer.Token

class TokenExpecting constructor(expectedToken: List<TokenType>, gotToken: Token?)
    : Error("Unexpected token. Wanted : ${expectedToken.joinToString()}. Got : " + (gotToken?.type ?: "nothing"))