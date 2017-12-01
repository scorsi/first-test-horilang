package com.scorsi.horilang.parser.ast

data class ConditionalBranchNode constructor(val condition: ConditionNode, val thenBlock: BlockNode?, val elseBlock: BlockNode? = null) : StatementNode()