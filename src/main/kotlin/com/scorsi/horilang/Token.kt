package com.scorsi.horilang

data class Token constructor(val type: TokenType, val value: String, val line: Int, val column: Int)