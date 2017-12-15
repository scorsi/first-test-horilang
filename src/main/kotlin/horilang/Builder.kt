package horilang

import horilang.lexer.TokenType
import horilang.lexer.Lexer
import horilang.lexer.LexerRule
import horilang.nodes.*
import horilang.parser.Parser
import horilang.parser.ParserInfo
import horilang.parser.ParserRule
import horilang.parser.MTree
import kotlin.Pair

object Builder {

    private var lexerRules: MutableList<LexerRule> = mutableListOf()
    private var parserRules: MutableMap<String, Pair<Class<Node>, MutableList<MTree<ParserRule>>>> = mutableMapOf()
    private var statements: MutableList<String> = mutableListOf()
    private lateinit var blockClass: Class<Node>
    private var isInitialized: Boolean = false

    private fun addLexerRule(rule: LexerRule) {
        lexerRules.add(rule)
    }

    private fun addParserRule(key: String, c: Class<Node>, rule: MTree<ParserRule>) {
        if (parserRules[key] == null) {
            parserRules.put(key, Pair(c, mutableListOf()))
        }
        parserRules[key]!!.second.add(rule)
    }

    private fun addParserRule(key: String, c: Class<Node>, rules: List<MTree<ParserRule>>) {
        if (parserRules[key] == null) {
            parserRules.put(key, Pair(c, mutableListOf()))
        }
        parserRules[key]!!.second.addAll(rules)
    }

    private fun registerParserBlockNode(blockClass: Class<Node>) {
        this.blockClass = blockClass
    }

    private fun registerParserStatement(statement: String) {
        statements.add(statement)
    }

    @JvmStatic
    fun build(input: String): Parser {
        if (!isInitialized) {
            /**
             * Create all the Lexer Rules
             */
            addLexerRule(LexerRule(TokenType.VAR, """var"""))
            addLexerRule(LexerRule(TokenType.IF, """if"""))
            addLexerRule(LexerRule(TokenType.ELSE, """else"""))
            addLexerRule(LexerRule(TokenType.FLOAT, """([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+)"""))
            addLexerRule(LexerRule(TokenType.INTEGER, """[0-9]+"""))
            addLexerRule(LexerRule(TokenType.SYMBOL, """[a-z]+"""))
            addLexerRule(LexerRule(TokenType.STRING, """\".*\""""))
            addLexerRule(LexerRule(TokenType.DOT, """\."""))
            addLexerRule(LexerRule(TokenType.LPAREN, """\("""))
            addLexerRule(LexerRule(TokenType.RPAREN, """\)"""))
            addLexerRule(LexerRule(TokenType.LBRACE, """\{"""))
            addLexerRule(LexerRule(TokenType.RBRACE, """\}"""))
            addLexerRule(LexerRule(TokenType.ASSIGN, """="""))
            addLexerRule(LexerRule(TokenType.EQUAL, """=="""))
            addLexerRule(LexerRule(TokenType.GREATER, """>"""))
            addLexerRule(LexerRule(TokenType.GREATER_EQUAL, """>="""))
            addLexerRule(LexerRule(TokenType.LOWER, """<"""))
            addLexerRule(LexerRule(TokenType.LOWER_EQUAL, """<="""))
            addLexerRule(LexerRule(TokenType.SUB, """-"""))
            addLexerRule(LexerRule(TokenType.ADD, """\+"""))
            addLexerRule(LexerRule(TokenType.MUL, """\*"""))
            addLexerRule(LexerRule(TokenType.DIV, """/"""))
            addLexerRule(LexerRule(TokenType.MOD, """%"""))
            addLexerRule(LexerRule(TokenType.BINARY_AND, """&"""))
            addLexerRule(LexerRule(TokenType.BINARY_OR, """|"""))
            addLexerRule(LexerRule(TokenType.BINARY_XOR, """^"""))
            addLexerRule(LexerRule(TokenType.BINARY_NOT, """~"""))
            addLexerRule(LexerRule(TokenType.BINARY_LEFT_SHIFT, """<<"""))
            addLexerRule(LexerRule(TokenType.BINARY_RIGHT_SHIFT, """>>"""))
            addLexerRule(LexerRule(TokenType.LOGICAL_NOT, """!"""))
            addLexerRule(LexerRule(TokenType.LOGICAL_AND, """&&"""))
            addLexerRule(LexerRule(TokenType.LOGICAL_OR, """||"""))
            addLexerRule(LexerRule(TokenType.EOI, """;"""))
            addLexerRule(LexerRule(TokenType.EOL, """\n"""))

            /**
             * Create all the Parser Rules
             */
            @Suppress("UNCHECKED_CAST")
            addParserRule("Declaration", VariableDeclaration::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.VAR), mutableListOf(
                            MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true), mutableListOf( // VAR SYMBOL
                                    MTree(ParserRule(token = TokenType.ASSIGN), mutableListOf(
                                            MTree(ParserRule(specialRule = "Expression")),
                                            MTree(ParserRule(specialRule = "Value"))
                                    ))
                            ))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("Assignment", VariableAssignment::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.SYMBOL), mutableListOf(
                            MTree(ParserRule(token = TokenType.ASSIGN), mutableListOf(
                                    MTree(ParserRule(specialRule = "Expression")),
                                    MTree(ParserRule(specialRule = "Value"))
                            ))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("Value", Value::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.INTEGER)),
                    MTree(ParserRule(token = TokenType.FLOAT)),
                    MTree(ParserRule(token = TokenType.STRING)),
                    MTree(ParserRule(token = TokenType.SYMBOL)),
                    MTree(ParserRule(specialRule = "SignOperator"), mutableListOf(
                            MTree(ParserRule(token = TokenType.INTEGER)),
                            MTree(ParserRule(token = TokenType.FLOAT)),
                            MTree(ParserRule(token = TokenType.SYMBOL))
                    ))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("Expression", Expression::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(specialRule = "Value", isEnd = true), mutableListOf(
                            MTree(ParserRule(specialRule = "Operator"), mutableListOf(
                                    MTree(ParserRule(specialRule = "Expression"))
                            ))
                    )),
                    MTree(ParserRule(token = TokenType.LPAREN), mutableListOf(
                            MTree(ParserRule(specialRule = "Expression"), mutableListOf(
                                    MTree(ParserRule(token = TokenType.RPAREN, isEnd = true), mutableListOf(
                                            MTree(ParserRule(specialRule = "Operator"), mutableListOf(
                                                    MTree(ParserRule(specialRule = "Expression"))
                                            ))
                                    ))
                            ))
                    ))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("SignOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.SUB)),
                    MTree(ParserRule(token = TokenType.ADD))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("ConditionOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.EQUAL)),
                    MTree(ParserRule(token = TokenType.GREATER)),
                    MTree(ParserRule(token = TokenType.LOWER)),
                    MTree(ParserRule(token = TokenType.GREATER_EQUAL)),
                    MTree(ParserRule(token = TokenType.LOWER_EQUAL))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("LowCalcOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.SUB)),
                    MTree(ParserRule(token = TokenType.ADD)),
                    MTree(ParserRule(token = TokenType.BINARY_AND)),
                    MTree(ParserRule(token = TokenType.BINARY_OR)),
                    MTree(ParserRule(token = TokenType.BINARY_XOR)),
                    MTree(ParserRule(token = TokenType.BINARY_NOT)),
                    MTree(ParserRule(token = TokenType.BINARY_LEFT_SHIFT)),
                    MTree(ParserRule(token = TokenType.BINARY_RIGHT_SHIFT))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("HighCalcOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.MUL)),
                    MTree(ParserRule(token = TokenType.DIV)),
                    MTree(ParserRule(token = TokenType.MOD))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("LogicalOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.LOGICAL_NOT)),
                    MTree(ParserRule(token = TokenType.LOGICAL_AND)),
                    MTree(ParserRule(token = TokenType.LOGICAL_OR))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("Operator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.EQUAL)),
                    MTree(ParserRule(token = TokenType.GREATER)),
                    MTree(ParserRule(token = TokenType.LOWER)),
                    MTree(ParserRule(token = TokenType.GREATER_EQUAL)),
                    MTree(ParserRule(token = TokenType.LOWER_EQUAL)),
                    MTree(ParserRule(token = TokenType.SUB)),
                    MTree(ParserRule(token = TokenType.ADD)),
                    MTree(ParserRule(token = TokenType.MUL)),
                    MTree(ParserRule(token = TokenType.DIV)),
                    MTree(ParserRule(token = TokenType.MOD)),
                    MTree(ParserRule(token = TokenType.BINARY_AND)),
                    MTree(ParserRule(token = TokenType.BINARY_OR)),
                    MTree(ParserRule(token = TokenType.BINARY_XOR)),
                    MTree(ParserRule(token = TokenType.BINARY_NOT)),
                    MTree(ParserRule(token = TokenType.BINARY_LEFT_SHIFT)),
                    MTree(ParserRule(token = TokenType.BINARY_RIGHT_SHIFT)),
                    MTree(ParserRule(token = TokenType.LOGICAL_NOT)),
                    MTree(ParserRule(token = TokenType.LOGICAL_AND)),
                    MTree(ParserRule(token = TokenType.LOGICAL_OR))
            ))

            /**
             * Register Parser Statements and Block
             */
            registerParserStatement("Assignment")
            registerParserStatement("Declaration")
            registerParserStatement("Value")
            registerParserStatement("Expression")

            @Suppress("UNCHECKED_CAST")
            registerParserBlockNode(Block::class.java as Class<Node>)


            isInitialized = true
        }
        @Suppress("UNCHECKED_CAST")
        return Parser(Lexer(input, lexerRules), ParserInfo(parserRules as Map<String, Pair<Class<Node>, ArrayList<MTree<ParserRule>>>>, statements, blockClass))
    }

}
