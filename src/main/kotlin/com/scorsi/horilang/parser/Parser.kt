package com.scorsi.horilang.parser

import com.scorsi.horilang.lexer.LexerStream
import com.scorsi.horilang.lexer.Token
import com.scorsi.horilang.lexer.TokenType
import com.scorsi.horilang.parser.ast.*

class Parser constructor(val lexer: LexerStream) {

    private var current: Token? = null

    private fun expectNextToken(expectedTypes: List<TokenType>): Boolean {
        current = lexer.next()
        return when (current) {
            null -> false
            else -> expectedTypes.contains((current as Token).type)
        }
    }

    private fun getType(): Token =
            expectNextToken(listOf(TokenType.TYPE)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.TYPE), current)
                    true -> current!!
                }
            }

    private fun getSymbol(): Token =
            expectNextToken(listOf(TokenType.SYMBOL)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.SYMBOL), current)
                    true -> current!!
                }
            }

    private fun getValue(): Token =
            expectNextToken(listOf(TokenType.NUMBER, TokenType.STRING, TokenType.SYMBOL)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.NUMBER, TokenType.STRING, TokenType.SYMBOL), current)
                    true -> current!!
                }
            }

    private fun getDeclarationType(): Token =
            expectNextToken(listOf(TokenType.DDOT)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.DDOT), current)
                    true -> getType()
                }
            }

    private fun getDeclaration(): DeclarationNode? =
            getSymbol().let { symbol ->
                getDeclarationType().let { type ->
                    expectNextToken(listOf(TokenType.ASSIGN)).let { // CHECK IF THE DECLARATION ALSO ASSIGN
                        when (it) {
                            false -> DeclarationNode(symbol, type)
                            true -> DeclarationNode(symbol, type, getValue())
                        }
                    }
                }
            }

    private fun getAssignmentValue(): Token =
            expectNextToken(listOf(TokenType.ASSIGN)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.ASSIGN), current)
                    true -> getValue()
                }
            }

    private fun getAssignment(): AssignmentNode? =
            current.let { symbol -> getAssignmentValue().let { value -> AssignmentNode(symbol as Token, value) } }

    private fun getStatement(): StatementNode? =
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

    private fun getStatements(list: MutableList<StatementNode>): MutableList<StatementNode> =
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

    private fun getBlock(): BlockNode? =
            when (current) {
                null -> null
                else -> BlockNode(getStatements(mutableListOf()))
            }

    fun parse(): Node? = lexer.next().let { current = it; getBlock() }

}