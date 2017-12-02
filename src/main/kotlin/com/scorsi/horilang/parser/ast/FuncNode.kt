package com.scorsi.horilang.parser.ast

/**
 * FUNC LPAREN (Argument){0,} RPAREN LBRACE body RBRACE
 */
data class FuncNode constructor(val symbol: SymbolNode, val arguments: List<ArgumentNode>, val body: BlockNode?) : StatementNode()