package com.scorsi.horilang.parser

import com.scorsi.horilang.lexer.LexerStream
import com.scorsi.horilang.lexer.Token
import com.scorsi.horilang.lexer.TokenType
import com.scorsi.horilang.parser.ast.*

class Parser constructor(val lexer: LexerStream) {

    private var current: Token? = null

    private fun expectNextToken(expectedTypes: List<TokenType>): Boolean =
            lexer.next().let {
                current = it
                when (current) {
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

    private fun getComparator(): Token =
            expectNextToken(listOf(TokenType.EQUAL, TokenType.NOTEQUAL, TokenType.GREATER, TokenType.GREATEREQUAL, TokenType.LOWER, TokenType.LOWEREQUAL)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.EQUAL, TokenType.NOTEQUAL, TokenType.GREATER, TokenType.GREATEREQUAL, TokenType.LOWER, TokenType.LOWEREQUAL), current)
                    true -> current!!
                }
            }

    private fun getLParen(): Token =
            expectNextToken(listOf(TokenType.LPAREN)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.LPAREN), current)
                    true -> current!!
                }
            }

    private fun getRParen(): Token =
            expectNextToken(listOf(TokenType.RPAREN)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.RPAREN), current)
                    true -> current!!
                }
            }

    private fun getLBrace(): Token =
            expectNextToken(listOf(TokenType.LBRACE)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.LBRACE), current)
                    true -> current!!
                }
            }

    private fun getRBrace(): Token =
            expectNextToken(listOf(TokenType.RBRACE)).let {
                when (it) {
                    false -> throw TokenExpecting(listOf(TokenType.RBRACE), current)
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

    private fun getDeclarationNode(): DeclarationNode? =
            getSymbol().let { symbol ->
                getDeclarationType().let { type ->
                    expectNextToken(listOf(TokenType.ASSIGN)).let {
                        // CHECK IF THE DECLARATION ALSO ASSIGN
                        when (it) {
                            false -> DeclarationNode(SymbolNode(symbol), type)
                            true -> lexer.next().let {
                                current = it
                                getStatementNode().let {
                                    when (it) {
                                        null -> throw Error(current.toString())
                                        is ValueNode -> DeclarationNode(SymbolNode(symbol), type, it)
                                        else -> throw Error()
                                    }
                                }
                            }
                        }
                    }
                }
            }

    private fun getAssignmentNode(symbol: Token): AssignmentNode? =
            lexer.next().let {
                current = it
                getStatementNode().let { value ->
                    when (value) {
                        null -> throw Error()
                        else -> AssignmentNode(symbol, value)
                    }
                }
            }

    private fun getConditionalBranchNodeCondition(): ConditionNode =
            getLParen().let { getValue().let { left -> getComparator().let { comparator -> getValue().let { right -> getRParen().let { ConditionNode(left, right, comparator) } } } } }

    private fun getBody(): BlockNode? =
            getLBrace().let {
                current = lexer.next()
                getBlockNode().let { block ->
                    when (current!!.type) {
                        TokenType.RBRACE -> block
                        else -> throw TokenExpecting(listOf(TokenType.RBRACE), current)
                    }
                }
            }

    private fun getConditionalBranchNode(): ConditionalBranchNode? =
            getConditionalBranchNodeCondition().let { condition ->
                getBody().let { thenBlock ->
                    expectNextToken(listOf(TokenType.ELSE)).let {
                        when (it) {
                            false -> ConditionalBranchNode(condition, thenBlock)
                            true -> ConditionalBranchNode(condition, thenBlock, getBody())
                        }
                    }
                }
            }

    private fun getValueNode(): ValueNode? =
            (current as Token).let {
                when (it.type) {
                    TokenType.NUMBER -> ValueNode(it)
                    TokenType.STRING -> ValueNode(it)
                    else -> null
                }
            }

    private fun getStatementNodeStartWithSymbol(): StatementNode? =
            current.let { symbol ->
                lexer.next().let {
                    when (it) {
                        null -> SymbolNode(symbol as Token)
                        else -> when (it.type) {
                            TokenType.ASSIGN -> getAssignmentNode(symbol as Token)
                            else -> SymbolNode(symbol as Token)
                        }
                    }
                }
            }

    private fun getStatementNode(): StatementNode? =
            when (current) {
                null -> null
                else -> when ((current as Token).type) {
                    TokenType.VAR -> getDeclarationNode()
                    TokenType.SYMBOL -> getStatementNodeStartWithSymbol()
                    TokenType.IF -> getConditionalBranchNode()
                    else -> getValueNode()
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

    private fun getAllStatementNodes(list: MutableList<StatementNode>): MutableList<StatementNode> =
            when (current) {
                null -> list
                else -> getStatementNode().let {
                    when (it) {
                        null -> list
                        else -> {
                            list.add(it)
                            current = lexer.next()
                            getAllStatementNodes(list)
                        }
                    }
                }
            }

    private fun getBlockNode(): BlockNode? =
            when (current) {
                null -> null
                else -> getAllStatementNodes(mutableListOf()).let {
                    when {
                        it.isEmpty() -> null
                        else -> BlockNode(it)
                    }
                }
            }

    fun parse(): Node? = lexer.next().let { current = it; getBlockNode() }

}