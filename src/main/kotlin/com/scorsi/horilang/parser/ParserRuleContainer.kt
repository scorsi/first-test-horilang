package com.scorsi.horilang.parser

import com.scorsi.horilang.ast.Node

data class ParserRuleContainer @JvmOverloads constructor(val rule: ParserRule, val isEnd: Boolean = false, val node: Class<Node>? = null)