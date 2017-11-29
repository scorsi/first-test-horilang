package com.scorsi.horilang.lexer.builder

import com.scorsi.horilang.lexer.Lexer
import com.scorsi.horilang.lexer.Rule

class LexerBuilder {

    private var rules: MutableList<Rule> = mutableListOf()

    fun add(rule: Rule) = rules.add(rule)

    fun build() = Lexer(rules.toList())

    fun defaultBuild() = enumValues<DefaultRule>().forEach { add(it.build()) }.let { build() }

}