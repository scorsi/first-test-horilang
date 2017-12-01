package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

/**
 * VAR SYMBOL DDOT TYPE (ASSIGN statement)?
 */
data class DeclarationNode constructor(val symbol: SymbolNode, val type: Token, val rightValue: StatementNode? = null) : StatementNode()