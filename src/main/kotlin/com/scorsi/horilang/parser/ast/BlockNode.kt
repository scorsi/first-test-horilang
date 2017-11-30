package com.scorsi.horilang.parser.ast

/**
 * Block = (Statement)*
 */
data class BlockNode constructor(val statements: List<StatementNode>) : Node()