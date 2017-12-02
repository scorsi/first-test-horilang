package com.scorsi.horilang.parser

data class ParserRuleTree<out T> constructor(val value: T, val children: List<ParserRuleTree<T>> = emptyList())