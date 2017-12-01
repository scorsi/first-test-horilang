package com.scorsi.horilang.parser.ast

/**
 * IF LPAREN condition RPAREN LBRACE block RBRACE (ELSE LBRACE block RBRACE)?
 */
data class ConditionalBranchNode constructor(val condition: ConditionNode, val thenBlock: BlockNode?, val elseBlock: BlockNode? = null) : StatementNode()