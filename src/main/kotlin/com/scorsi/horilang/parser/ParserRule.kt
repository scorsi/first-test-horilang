package com.scorsi.horilang.parser

import com.scorsi.horilang.TokenType

class ParserRule @JvmOverloads constructor(var token: TokenType? = null, var specialRule: String? = null)