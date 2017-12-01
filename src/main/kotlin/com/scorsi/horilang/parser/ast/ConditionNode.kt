package com.scorsi.horilang.parser.ast

import com.scorsi.horilang.lexer.Token

/**
 * (INTEGER | STRING | SYMBOL) (EQUAL | GREATER | GREATEREQUAL | LOWER | LOWEREQUAL) (INTEGER | STRING | SYMBOL)
 */
data class ConditionNode constructor(val left: Token, val right: Token, val operator: Token) : Node()