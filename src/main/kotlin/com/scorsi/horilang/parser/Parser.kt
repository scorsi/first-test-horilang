package com.scorsi.horilang.parser

import com.scorsi.horilang.TokenType
import com.scorsi.horilang.ast.Node
import com.scorsi.horilang.lexer.Lexer
import kotlin.reflect.full.primaryConstructor

class Parser constructor(val lexer: Lexer, private val rules: Map<String, Pair<Class<Node>, ArrayList<ParserRuleTree<ParserRuleContainer>>>>) {

    private fun parseTreeNext(classToCreate: Class<Node>, matchedRule: ParserRuleTree<ParserRuleContainer>, baseLevel: Int, actualLevel: Int, nodes: MutableList<Node>): Pair<Node?, Boolean> {
        var match = false
        matchedRule.children
                .forEach { rule ->
                    parseTree(classToCreate, rule, baseLevel, actualLevel + 1, nodes).let {
                        if (it.second)
                            match = true
                        if (it.first != null)
                            return it
                    }
                }
        return when (match) {
            true -> Pair(null, true)
            false -> when (matchedRule.value.isEnd) {
                true -> Pair(classToCreate.kotlin.primaryConstructor!!.call().build(lexer.consume(baseLevel, actualLevel), nodes), true)
                false -> when (matchedRule.children.size) {
                    0 -> Pair(classToCreate.kotlin.primaryConstructor!!.call().build(lexer.consume(baseLevel, actualLevel), nodes), true)
                    else -> Pair(null, false)
                }
            }
        }
    }

    private fun parseTreeToken(classToCreate: Class<Node>, matchedRule: ParserRuleTree<ParserRuleContainer>, baseLevel: Int, actualLevel: Int, nodes: MutableList<Node>): Pair<Node?, Boolean> =
            lexer.peek(actualLevel).let { actualToken ->
                when (actualToken) {
                    null -> Pair(null, false)
                    else -> when {
                        actualToken.type == matchedRule.value.rule.token -> parseTreeNext(classToCreate, matchedRule, baseLevel, actualLevel, nodes)
                        else -> Pair(null, false)
                    }
                }
            }

    private fun parseTreeSpecialRule(classToCreate: Class<Node>, matchedRule: ParserRuleTree<ParserRuleContainer>, baseLevel: Int, actualLevel: Int, nodes: MutableList<Node>): Pair<Node?, Boolean> =
            rules.filter { it.key == matchedRule.value.rule.specialRule }
                    .forEach { rule ->
                        rule.value.second.forEach { ruleToTest ->
                            parseTree(rule.value.first, ruleToTest, actualLevel - 1, actualLevel, nodes).let {
                                if (it.first != null) {
                                    nodes.add(it.first!!)
                                    return parseTreeNext(classToCreate, matchedRule, baseLevel, actualLevel - 1, nodes)
                                }
                            }
                        }
                    }.let { Pair(null, false) }

    private fun parseTree(classToCreate: Class<Node>, matchedRule: ParserRuleTree<ParserRuleContainer>, baseLevel: Int, actualLevel: Int, nodes: MutableList<Node>): Pair<Node?, Boolean> =
            when (matchedRule.value.rule.token) {
                null -> when (matchedRule.value.rule.specialRule) {
                    null -> throw Error()
                    else -> parseTreeSpecialRule(classToCreate, matchedRule, baseLevel, actualLevel, nodes)
                }
                else -> parseTreeToken(classToCreate, matchedRule, baseLevel, actualLevel, nodes)
            }

    private fun parseStatement(): Node? =
            rules.forEach { rule ->
                rule.value.second
                        .forEach { ruleToTest ->
                            parseTree(rule.value.first, ruleToTest, 0, 1, mutableListOf()).let {
                                if (it.first != null)
                                    return it.first
                            }
                        }
            }.let { throw ParserInstructionNotFound(lexer.tokens) }

    private fun parseBlock(list: MutableList<Node>): List<Node> =
            when (lexer.isStreamFinished()) {
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
                                            lexer.consume(0, 1)
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

    fun parse(): List<Node> = parseBlock(mutableListOf())

}