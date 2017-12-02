package com.scorsi.horilang.lexer

class LexerSyntaxError constructor(found: String) : Error("Cannot recognize \"$found\", are you sure you didn't misspelled it?")