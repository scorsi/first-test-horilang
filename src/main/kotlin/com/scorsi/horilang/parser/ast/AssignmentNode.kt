package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

/**
 * SYMBOL ASSIGN (INTEGER | STRING | SYMBOL)
 */
data class AssignmentNode constructor(val symbol: Token, val value: Token) : StatementNode()