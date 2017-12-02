package com.scorsi.horilang.parser

import com.scorsi.horilang.TokenType
import com.scorsi.horilang.ast.Node

data class ParserRule @JvmOverloads constructor(val token: TokenType, val isEnd: Boolean = false, val node: Class<Node>? = null)