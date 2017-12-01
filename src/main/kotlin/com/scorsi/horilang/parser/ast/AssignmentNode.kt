package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

/**
 * SYMBOL EQUAL statement
 */
data class AssignmentNode constructor(val leftValue: Token, val rightValue: StatementNode) : StatementNode()