package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

/**
 * VAR SYMBOL DDOT TYPE (ASSIGN (INTEGER | STRING | SYMBOL))?
 */
data class DeclarationNode constructor(val symbol: Token, val type: Token, val value: Token? = null) : StatementNode()