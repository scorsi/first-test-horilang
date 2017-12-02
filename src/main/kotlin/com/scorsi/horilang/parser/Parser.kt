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
                            false -> DeclarationNode(SymbolNode(symbol.value as String), type)
                            true -> lexer.next().let {
                                current = it
                                getStatementNodeWithoutSemicolon().let {
                                    when (it) {
                                        null -> throw Error("Expected right value")
                                    // CHECK HERE TO ALLOW DIFFERENT RIGHTVALUE FOR DECLARATIONNODE
                                        else -> DeclarationNode(SymbolNode(symbol.value as String), type, it)
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
                getStatementNodeWithoutSemicolon().let { value ->
                    when (value) {
                        null -> throw Error("An assignment must have a rvalue.")
                        else -> AssignmentNode(symbol, value)
                    }
                }
            }

    private fun getConditionalBranchNodeCondition(): ConditionNode =
            getLParen().let {
                getValue().let { left ->
                    getComparator().let { comparator ->
                        getValue().let { right ->
                            getRParen().let { ConditionNode(left, right, comparator) }
                        }
                    }
                }
            }

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
                    TokenType.NUMBER -> ValueNode(it.value as String, it.type)
                    TokenType.STRING -> ValueNode(it.value as String, it.type)
                    else -> null
                }
            }

    private fun getFuncNodeArguments(list: MutableList<ArgumentNode>): MutableList<ArgumentNode> =
            when (current) {
                null -> list
                else -> expectNextToken(listOf(TokenType.SYMBOL)).let {
                    val symbol = current as Token
                    when (it) {
                        false -> list
                        true -> expectNextToken(listOf(TokenType.DDOT)).let {
                            expectNextToken(listOf(TokenType.TYPE)).let {
                                list.add(ArgumentNode(SymbolNode(symbol.value as String), current as Token))
                                list
                            }
                        }
                    }
                }
            }

    private fun getFuncNodeArgument(): MutableList<ArgumentNode> =
            getLParen().let {
                getFuncNodeArguments(mutableListOf()).let { list ->
                    when (current) {
                        null -> throw TokenExpecting(listOf(TokenType.RPAREN), current)
                        else -> when ((current as Token).type) {
                            TokenType.RPAREN -> list
                            else -> getRParen().let { list }
                        }
                    }
                }
            }

    private fun getFuncNode(): FuncNode? =
            getSymbol().let { symbol ->
                getFuncNodeArgument().let { arguments ->
                    FuncNode(SymbolNode(symbol.value as String), arguments, getBody())
                }
            }

    private fun getStatementNodeStartWithSymbol(): StatementNode? =
            current.let { symbol ->
                lexer.next().let {
                    when (it) {
                        null -> SymbolNode(symbol!!.value as String)
                        else -> when (it.type) {
                            TokenType.ASSIGN -> getAssignmentNode(symbol as Token)
                            else -> SymbolNode(symbol!!.value as String)
                        }
                    }
                }
            }

    private fun getStatementNodeWithoutSemicolon(): StatementNode? =
            when (current) {
                null -> null
                else -> when (current!!.type) {
                    TokenType.FUNC -> getFuncNode()
                    TokenType.VAR -> getDeclarationNode()
                    TokenType.SYMBOL -> getStatementNodeStartWithSymbol()
                    TokenType.IF -> getConditionalBranchNode()
                    else -> getValueNode()
                }
            }

    private fun getStatementNode(): StatementNode? =
            getStatementNodeWithoutSemicolon().let { ret ->
                when (ret) {
                    null -> null
                    else -> when {
                        current == null -> ret
                        current!!.type == TokenType.SEMICOLON -> ret
                        else -> expectNextToken(listOf(TokenType.SEMICOLON)).let {
                            when {
                                current == null || it -> ret
                                else -> throw Error("Statement syntax error : missing \";\"")
                            }
                        }
                    }
                }
            }

    private fun getAllStatementNodes(list: MutableList<StatementNode> = mutableListOf()): MutableList<StatementNode> =
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
                else -> getAllStatementNodes().let {
                    when {
                        it.isEmpty() -> null
                        else -> BlockNode(it)
                    }
                }
            }

    fun parse(): Node? = lexer.next().let { current = it; getBlockNode() }

}