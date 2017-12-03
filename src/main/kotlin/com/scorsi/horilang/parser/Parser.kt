package com.scorsi.horilang.parser

import com.scorsi.horilang.TokenType
import com.scorsi.horilang.ast.Node
import com.scorsi.horilang.lexer.Lexer
import kotlin.reflect.full.primaryConstructor

class Parser constructor(val lexer: Lexer, private val rules: Map<String, Pair<Class<Node>, ArrayList<ParserRuleTree<ParserRuleContainer>>>>) {

    private fun parseTreeToken(classToCreate: Class<Node>, matchedRule: ParserRuleTree<ParserRuleContainer>, level: Int): Pair<Node?, Boolean> =
            lexer.peek(level).let { newToken ->
                when (newToken) {
                    null -> Pair(null, false)
                    else -> when {
                        newToken.type == matchedRule.value.rule.token -> {
                            var match = false
                            matchedRule.children
                                    .forEach { rule ->
                                        parseTree(classToCreate, rule, level + 1).let {
                                            if (it.second)
                                                match = true
                                            if (it.first != null)
                                                return it
                                        }
                                    }
                            when (match) {
                                true -> Pair(null, true)
                                false -> when (matchedRule.value.isEnd) {
                                    true -> Pair(classToCreate.kotlin.primaryConstructor!!.call().build(lexer.purge(level)), true)
                                    false -> Pair(null, true)
                                }
                            }
                        }
                        else -> Pair(null, false)
                    }
                }
            }

    private fun parseTree(classToCreate: Class<Node>, matchedRule: ParserRuleTree<ParserRuleContainer>, level: Int): Pair<Node?, Boolean> =
            when (matchedRule.value.rule.token) {
                null -> when (matchedRule.value.rule.specialRule) {
                    null -> throw Error()
                    else -> TODO("Not implemented yet")
                }
                else -> parseTreeToken(classToCreate, matchedRule, level)
            }

    private fun parseStatement(): Node? =
            lexer.nextToken().let { _ ->
                rules.forEach { rule ->
                    rule.value.second
                            .forEach { ruleToTest ->
                                parseTree(rule.value.first, ruleToTest, 1).let {
                                    if (it.first != null)
                                        return it.first
                                }
                            }
                }
                throw ParserInstructionNotFound(lexer.tokens)
            }

    private fun parseBlock(list: MutableList<Node>): List<Node> =
            lexer.nextToken().let {
                when (lexer.streamFinished) {
                    true -> list
                    false -> parseStatement().let { statement ->
                        when (statement) {
                            null -> list
                            else -> {
                                println("Creating: $statement")
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