package com.scorsi.horilang.parser

import com.scorsi.horilang.ast.Node
import com.scorsi.horilang.lexer.Lexer
import kotlin.reflect.full.primaryConstructor

class Parser constructor(val lexer: Lexer, private val rules: ArrayList<ParserRuleTree<ParserRule>>) {

    private fun parseTree(matchedRule: ParserRuleTree<ParserRule>): Class<Node>? =
            lexer.peek().let { token ->
                if (matchedRule.value.token == token.type) {
                    var matchedClass: Class<Node>? = null
                    var match = false
                    lexer.next().let { newToken ->
                        matchedRule.children
                                .filter { it.value.token == newToken.type }
                                .forEach { rule ->
                                    match = true
                                    parseTree(rule).let {
                                        if (it != null) {
                                            matchedClass = it
                                            return@forEach
                                        } else null
                                    }
                                }
                    }
                    when (matchedClass) {
                        null -> when (match) {
                            true -> null
                            false -> when (matchedRule.value.isEnd) {
                                true -> matchedRule.value.node
                                false -> null
                            }
                        }
                        else -> matchedClass
                    }
                } else null
            }

    fun parse(): Node? =
            lexer.next().let {
                var matchedClass: Class<Node>? = null
                rules.forEach { rule ->
                    parseTree(rule).let {
                        when (it) {
                            null -> null
                            else -> {
                                matchedClass = it
                                return@forEach
                            }
                        }
                    }
                }
                when (matchedClass) {
                    null -> null
                    else -> {
                        val node = matchedClass!!.kotlin.primaryConstructor!!.call().build(lexer.tokens)
                        lexer.clear()
                        node
                    }
                }
            }

}