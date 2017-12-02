package com.scorsi.horilang.parser

import com.scorsi.horilang.ast.Node
import com.scorsi.horilang.lexer.Lexer
import kotlin.reflect.full.primaryConstructor

class Parser constructor(val lexer: Lexer, private val rules: ArrayList<ParserRuleTree<ParserRule>>) {

    private fun parseTree(matchedRule: ParserRuleTree<ParserRule>, level: Int): Class<Node>? =
            lexer.peek(level + 1).let { newToken ->
                when (newToken) {
                    null -> when (matchedRule.value.isEnd) {
                        true -> matchedRule.value.node
                        false -> null
                    }
                    else -> {
                        var matchedClass: Class<Node>? = null
                        var match = false
                        matchedRule.children
                                .filter { it.value.token == newToken?.type }
                                .forEach { rule ->
                                    match = true
                                    parseTree(rule, level + 1).let {
                                        if (it != null) {
                                            matchedClass = it
                                            return@forEach
                                        } else null
                                    }
                                }
                        return when (matchedClass) {
                            null -> when (match) {
                                true -> null
                                false -> when (matchedRule.value.isEnd) {
                                    true -> matchedRule.value.node
                                    false -> null
                                }
                            }
                            else -> matchedClass
                        }
                    }
                }
            }

    fun parse(): Node? =
            lexer.peek(1).let { token ->
                var matchedClass: Class<Node>? = null
                rules.filter { it.value.token == token?.type }
                        .forEach { rule ->
                            parseTree(rule, 1).let {
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
                    null -> throw ParserInstructionNotFound(lexer.tokens)
                    else -> {
                        val node = matchedClass!!.kotlin.primaryConstructor!!.call().build(lexer.tokens)
                        lexer.clear()
                        node
                    }
                }
            }

}