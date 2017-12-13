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
                matchedRule.value.isEnd || matchedRule.children.isEmpty() -> Pair(classToCreate.kotlin.primaryConstructor!!.call().build(lexer.consume(baseLevel, actualLevel), nodes), true)
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
                    else -> parseTreeSpecialRule(classToCreate, matchedRule, baseLevel, actualLevel, nodes)
                }
                else -> parseTreeToken(classToCreate, matchedRule, baseLevel, actualLevel, nodes)
            }

    private fun parseStatement(): Node? =
            info.rules.filter { info.statements.contains(it.key) }
                    .forEach { rule ->
                        rule.value.second
                                .forEach { ruleToTest ->
                                    parseTree(rule.value.first, ruleToTest, 0, 1, mutableListOf()).let {
                                        if (it.first != null)
                                            return it.first
                                    }
                                }
                    }.let { throw ParserInstructionNotFound(lexer.tokens) }

    private fun createBlock(list: MutableList<Node>): Node = info.block.kotlin.primaryConstructor!!.call().build(lexer.tokens, list)

    private fun parseBlock(list: MutableList<Node>): Node =
            when (lexer.isStreamFinished()) {
                true -> createBlock(list)
                false -> parseStatement().let { statement ->
                    when (statement) {
                        null -> createBlock(list)
                        else -> {
                            println("Creating: $statement")
                            list.add(statement)
                            lexer.peek(1).let { token ->
                                when (token) {
                                    null -> createBlock(list)
                                    else -> when (token.type) {
                                        TokenType.EOF -> createBlock(list)
                                        TokenType.EOI -> {
                                            lexer.consume(0, 1)
                                            parseBlock(list)
                                        }
                                        else -> createBlock(list)
                                    }
                                }
                            }
                        }
                    }
                }
            }

    fun parse(): Node = parseBlock(mutableListOf())

}