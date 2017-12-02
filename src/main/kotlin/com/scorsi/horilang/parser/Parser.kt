package com.scorsi.horilang.parser

import com.scorsi.horilang.TokenType
import com.scorsi.horilang.ast.Node
import com.scorsi.horilang.lexer.Lexer
import kotlin.reflect.full.primaryConstructor

class Parser constructor(val lexer: Lexer, private val rules: ArrayList<ParserRuleTree<ParserRule>>) {

    private fun parseTree(matchedRule: ParserRuleTree<ParserRule>, level: Int): Node? =
            lexer.peek(level + 1).let { newToken ->
                when (newToken) {
                    null -> when (matchedRule.value.isEnd) {
                        true -> matchedRule.value.node!!.kotlin.primaryConstructor!!.call().build(lexer.purge(level))
                        false -> null
                    }
                    else -> {
                        var matchedClass: Node? = null
                        var match = false
                        matchedRule.children
                                .filter { it.value.token == newToken.type }
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
                                    true -> matchedRule.value.node!!.kotlin.primaryConstructor!!.call().build(lexer.purge(level))
                                    false -> null
                                }
                            }
                            else -> matchedClass
                        }
                    }
                }
            }

    fun parseStatement(): Node? =
            lexer.peek(1).let { token ->
                var matchedClass: Node? = null
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
                    else -> matchedClass
                }
            }

    fun parseBlock(list: MutableList<Node>): List<Node> =
            lexer.next().let {
                when (lexer.streamFinished) {
                    true -> list
                    false -> parseStatement().let { statement ->
                        when (statement) {
                            null -> list
                            else -> {
                                list.add(statement)
                                lexer.peek(1).let { token ->
                                    when (token) {
                                        null -> list
                                        else -> when (token.type) {
                                            TokenType.EOF -> list
                                            TokenType.EOI -> {
                                                lexer.purge(1)
                                                parseBlock(list)
                                            }
                                            else -> list
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

    fun parse(): List<Node> = parseBlock(mutableListOf())

}