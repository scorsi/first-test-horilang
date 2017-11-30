package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

data class ConditionNode constructor(val left: Token, val right: Token, val operator: Token) : Node()