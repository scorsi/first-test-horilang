package com.scorsi.horilang.parser

import com.scorsi.horilang.TokenType
import com.scorsi.horilang.ast.Node
import com.scorsi.horilang.lexer.Lexer
import kotlin.reflect.full.primaryConstructor

class Parser constructor(val lexer: Lexer, private val info: ParserInfo) {

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
            false -> when {
                matchedRule.value.isEnd || matchedRule.children.isEmpty() ->
                    Pair(classToCreate.kotlin.primaryConstructor!!.call().build(lexer.consume(baseLevel, actualLevel), nodes), true).let {
                        println("Creating: ${it.first}")
                        it
                    }
                else -> Pair(null, false)
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
            info.rules.filter { it.key == matchedRule.value.rule.specialRule }
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
                    "Block" -> parseBlock(mutableListOf(), actualLevel - 1, actualLevel).let {
                        nodes.add(it)
                        parseTreeNext(classToCreate, matchedRule, baseLevel, actualLevel - 1, nodes)
                    }
                    "Statement" -> parseStatement(actualLevel - 1, actualLevel).let {
                        when (it.first) {
                            null -> it
                            else -> {
                                nodes.add(it.first!!)
                                parseTreeNext(classToCreate, matchedRule, baseLevel, actualLevel - 1, nodes)
                            }
                        }
                    }
                    else -> parseTreeSpecialRule(classToCreate, matchedRule, baseLevel, actualLevel, nodes)
                }
                else -> parseTreeToken(classToCreate, matchedRule, baseLevel, actualLevel, nodes)
            }

    private fun parseStatement(baseLevel: Int, actualLevel: Int): Pair<Node?, Boolean> =
            info.rules.filter { info.statements.contains(it.key) }
                    .forEach { rule ->
                        rule.value.second
                                .forEach { ruleToTest ->
                                    parseTree(rule.value.first, ruleToTest, baseLevel, actualLevel, mutableListOf()).let {
                                        if (it.first != null)
                                            return it
                                    }
                                }
                    }.let { Pair(null, false) }

    private fun createBlock(statements: MutableList<Node>): Node =
            info.block.kotlin.primaryConstructor!!.call().build(lexer.tokens, statements)

    private fun parseBlock(statements: MutableList<Node>, baseLevel: Int, actualLevel: Int): Node =
            when (lexer.isStreamFinished()) {
                true -> createBlock(statements)
                false -> parseStatement(baseLevel, actualLevel).let { statement ->
                    when (statement.first) {
                        null -> createBlock(statements)
                        else -> {
                            statements.add(statement.first!!)
                            lexer.peek(actualLevel).let { token ->
                                when (token) {
                                    null -> createBlock(statements)
                                    else -> when (token.type) {
                                        TokenType.EOI -> {
                                            lexer.consume(baseLevel, actualLevel)
                                            parseBlock(statements, baseLevel, actualLevel)
                                        }
                                        else -> createBlock(statements)
                                    }
                                }
                            }
                        }
                    }
                }
            }

    fun parse(): Node =
            parseBlock(mutableListOf(), 0, 1)

}