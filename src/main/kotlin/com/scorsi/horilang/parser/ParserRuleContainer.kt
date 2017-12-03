package com.scorsi.horilang.parser

data class ParserRuleContainer @JvmOverloads constructor(val rule: ParserRule, val isEnd: Boolean = false)