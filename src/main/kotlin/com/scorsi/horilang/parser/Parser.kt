package com.scorsi.horilang.parser

import com.scorsi.horilang.lexer.LexerStream
import com.scorsi.horilang.lexer.Token
import com.scorsi.horilang.lexer.TokenType
import com.scorsi.horilang.parser.ast.*

class Parser constructor(val lexer: LexerStream) {

    private var current: Token? = null

    private fun expectNextToken(expectedTypes: List<TokenType>) : Boolean {
        current = lexer.next()
        return when (current) {
            null -> false
            else -> expectedTypes.contains((current as Token).type)
        }
    }

    private fun getAssignment() : AssignmentNode? =
            when (current) {
                null -> throw Error("Assignment syntax error")
                else -> {
                    val symbol = current as Token
                    expectNextToken(listOf(TokenType.ASSIGN)).let {
                        when (it) {
                            false -> throw Error("Assignment syntax error")
                            true -> expectNextToken(listOf(TokenType.NUMBER, TokenType.STRING, TokenType.SYMBOL)).let {
                                when (it) {
                                    false -> throw Error("Assignment syntax error")
                                    true -> AssignmentNode(symbol, current as Token)
                                }
                            }
                        }
                    }
                }
            }

    private fun getDeclaration() : DeclarationNode? =
            when (current) {
                null -> null
                else -> {
                    expectNextToken(listOf(TokenType.SYMBOL)).let {
                        when (it) {
                            false -> throw Error("Declaration syntax error")
                            true -> {
                                val symbol = current as Token
                                expectNextToken(listOf(TokenType.DDOT)).let {
                                    when (it) {
                                        false -> throw Error("Declaration syntax error")
                                        true -> expectNextToken(listOf(TokenType.TYPE)).let {
                                            when (it) {
                                                false -> throw Error("Declaration syntax error")
                                                true -> {
                                                    val type = current as Token
                                                    expectNextToken(listOf(TokenType.ASSIGN)).let {
                                                        when (it) {
                                                            false -> DeclarationNode(symbol, type)
                                                            true -> expectNextToken(listOf(TokenType.NUMBER, TokenType.STRING, TokenType.SYMBOL)).let {
                                                                when (it) {
                                                                    false -> throw Error("Declaration syntax error")
                                                                    true -> DeclarationNode(symbol, type, current)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

    private fun getStatement() : StatementNode? =
            when (current) {
                null -> null
                else -> when ((current as Token).type) {
                    TokenType.VAR -> getDeclaration()
                    TokenType.SYMBOL -> getAssignment()
                    else -> null
                }.let { ret ->
                    when (ret) {
                        null -> null
                        else -> when {
                            current == null -> ret
                            (current as Token).type == TokenType.SEMICOLON -> ret
                            else -> expectNextToken(listOf(TokenType.SEMICOLON)).let {
                                when {
                                    current == null || it -> ret
                                    else -> throw Error("Statement syntax error : missing \";\"")
                                }
                            }
                        }
                    }
                }
            }

    private fun getStatements(list: MutableList<StatementNode>) : MutableList<StatementNode> =
            when (current) {
                null -> list
                else -> getStatement().let {
                    when (it) {
                        null -> list
                        else -> {
                            list.add(it)
                            current = lexer.next()
                            getStatements(list)
                        }
                    }
                }
            }

    private fun getBlock() : BlockNode? =
            when (current) {
                null -> null
                else -> BlockNode(getStatements(mutableListOf()))
            }

    fun parse() : Node? = lexer.next().let { current = it; getBlock() }

}