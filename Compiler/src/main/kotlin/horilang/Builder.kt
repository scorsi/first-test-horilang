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
            addLexerRule(LexerRule(TokenType.VAL, """val"""))
            addLexerRule(LexerRule(TokenType.FUN, """fun"""))
            addLexerRule(LexerRule(TokenType.IF, """if"""))
            addLexerRule(LexerRule(TokenType.ELSE, """else"""))
            addLexerRule(LexerRule(TokenType.MODULE, """module"""))
            addLexerRule(LexerRule(TokenType.EXPORT, """export"""))
            addLexerRule(LexerRule(TokenType.IMPORT, """import"""))
            addLexerRule(LexerRule(TokenType.FLOAT, """(\+|\-)?(([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+))"""))
            addLexerRule(LexerRule(TokenType.INTEGER, """(\+|\-)?([0-9]+)"""))
            addLexerRule(LexerRule(TokenType.SYMBOL, """[a-z][a-zA-Z]*"""))
            addLexerRule(LexerRule(TokenType.BIG_SYMBOL, """[a-z][a-zA-Z]*(\.([a-z][a-zA-Z]*)?)*"""))
            addLexerRule(LexerRule(TokenType.STRING, """\".*\""""))
            addLexerRule(LexerRule(TokenType.DOT, """\."""))
            addLexerRule(LexerRule(TokenType.LPAREN, """\("""))
            addLexerRule(LexerRule(TokenType.RPAREN, """\)"""))
            addLexerRule(LexerRule(TokenType.LBRACE, """\{"""))
            addLexerRule(LexerRule(TokenType.RBRACE, """\}"""))
            addLexerRule(LexerRule(TokenType.LBRACK, """\["""))
            addLexerRule(LexerRule(TokenType.RBRACK, """\]"""))
            addLexerRule(LexerRule(TokenType.COMMA, ""","""))
            addLexerRule(LexerRule(TokenType.LCHEV, """<"""))
            addLexerRule(LexerRule(TokenType.RCHEV, """>"""))
            addLexerRule(LexerRule(TokenType.LARROW, """<-"""))
            addLexerRule(LexerRule(TokenType.RARROW, """->"""))
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
            addParserRule("ModuleDeclaration", ModuleDeclaration::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.MODULE), mutableListOf(
                            MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true)),
                            MTree(ParserRule(token = TokenType.BIG_SYMBOL, isEnd = true))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("ImportDeclaration", ImportDeclaration::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.IMPORT), mutableListOf(
                            MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true)),
                            MTree(ParserRule(token = TokenType.BIG_SYMBOL, isEnd = true))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("ExportDeclaration", ExportDeclaration::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.EXPORT), mutableListOf(
                            MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true)),
                            MTree(ParserRule(token = TokenType.BIG_SYMBOL, isEnd = true)),
                            MTree(ParserRule(specialRule = "VariableDeclaration", isEnd = true)),
                            MTree(ParserRule(specialRule = "FunctionDeclaration", isEnd = true))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("VariableDeclaration", VariableDeclaration::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.VAR), mutableListOf(
                            MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true), mutableListOf( // VAR SYMBOL
                                    MTree(ParserRule(token = TokenType.ASSIGN), mutableListOf(
                                            MTree(ParserRule(specialRule = "Expression", isEnd = true)),
                                            MTree(ParserRule(specialRule = "Value", isEnd = true))
                                    ))
                            ))
                    )),
                    MTree(ParserRule(token = TokenType.VAL), mutableListOf(
                            MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true), mutableListOf( // VAR SYMBOL
                                    MTree(ParserRule(token = TokenType.ASSIGN), mutableListOf(
                                            MTree(ParserRule(specialRule = "Expression", isEnd = true)),
                                            MTree(ParserRule(specialRule = "Value", isEnd = true))
                                    ))
                            ))
                    ))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("VariableAssignment", VariableAssignment::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.SYMBOL), mutableListOf(
                            MTree(ParserRule(token = TokenType.ASSIGN), mutableListOf(
                                    MTree(ParserRule(specialRule = "Expression", isEnd = true)),
                                    MTree(ParserRule(specialRule = "Value", isEnd = true))
                            ))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("Expression", Expression::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(specialRule = "Value", isEnd = true), mutableListOf(
                            MTree(ParserRule(specialRule = "Operator"), mutableListOf(
                                    MTree(ParserRule(specialRule = "Expression", isEnd = true))
                            ))
                    )),
                    MTree(ParserRule(token = TokenType.LPAREN), mutableListOf(
                            MTree(ParserRule(specialRule = "Expression"), mutableListOf(
                                    MTree(ParserRule(token = TokenType.RPAREN, isEnd = true), mutableListOf(
                                            MTree(ParserRule(specialRule = "Operator"), mutableListOf(
                                                    MTree(ParserRule(specialRule = "Expression", isEnd = true))
                                            ))
                                    ))
                            ))
                    ))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("Value", Value::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.INTEGER, isEnd = true)),
                    MTree(ParserRule(token = TokenType.FLOAT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.STRING, isEnd = true)),
                    MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true)),
                    MTree(ParserRule(specialRule = "SignOperator"), mutableListOf(
                            MTree(ParserRule(token = TokenType.INTEGER, isEnd = true)),
                            MTree(ParserRule(token = TokenType.FLOAT, isEnd = true)),
                            MTree(ParserRule(token = TokenType.SYMBOL, isEnd = true))
                    ))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("ConditionalBranch", ConditionalBranch::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.IF), mutableListOf(
                            MTree(ParserRule(specialRule = "Expression"), mutableListOf(
                                    MTree(ParserRule(token = TokenType.RARROW), mutableListOf(
                                            MTree(ParserRule(specialRule = "Statement", isEnd = true), mutableListOf(
                                                    MTree(ParserRule(token = TokenType.ELSE), mutableListOf(
                                                            MTree(ParserRule(token = TokenType.RARROW), mutableListOf(
                                                                    MTree(ParserRule(specialRule = "Statement", isEnd = true))
                                                            )),
                                                            MTree(ParserRule(token = TokenType.LBRACE), mutableListOf(
                                                                    MTree(ParserRule(specialRule = "Block"), mutableListOf(
                                                                            MTree(ParserRule(token = TokenType.RBRACE, isEnd = true))
                                                                    ))
                                                            ))
                                                    ))
                                            ))
                                    )),
                                    MTree(ParserRule(token = TokenType.LBRACE), mutableListOf(
                                            MTree(ParserRule(specialRule = "Block"), mutableListOf(
                                                    MTree(ParserRule(token = TokenType.RBRACE, isEnd = true), mutableListOf(
                                                            MTree(ParserRule(token = TokenType.ELSE), mutableListOf(
                                                                    MTree(ParserRule(token = TokenType.RARROW), mutableListOf(
                                                                            MTree(ParserRule(specialRule = "Statement", isEnd = true))
                                                                    )),
                                                                    MTree(ParserRule(token = TokenType.LBRACE), mutableListOf(
                                                                            MTree(ParserRule(specialRule = "Block"), mutableListOf(
                                                                                    MTree(ParserRule(token = TokenType.RBRACE, isEnd = true))
                                                                            ))
                                                                    ))
                                                            ))
                                                    ))
                                            ))
                                    ))
                            ))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("FunctionArguments", FunctionArguments::class.java as Class<Node>,
                    MTree(ParserRule(specialRule = "VariableDeclaration", isEnd = true), mutableListOf(
                            MTree(ParserRule(token = TokenType.COMMA), mutableListOf(
                                    MTree(ParserRule(specialRule = "FunctionArguments", isEnd = true))
                            ))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("FunctionDeclaration", FunctionDeclaration::class.java as Class<Node>,
                    MTree(ParserRule(token = TokenType.FUN), mutableListOf(
                            MTree(ParserRule(token = TokenType.SYMBOL), mutableListOf(
                                    MTree(ParserRule(token = TokenType.LPAREN), mutableListOf(
                                            MTree(ParserRule(specialRule = "FunctionArguments"), mutableListOf(
                                                    MTree(ParserRule(token = TokenType.RPAREN), mutableListOf(
                                                            MTree(ParserRule(token = TokenType.RARROW), mutableListOf(
                                                                    MTree(ParserRule(specialRule = "Statement", isEnd = true))
                                                            )),
                                                            MTree(ParserRule(token = TokenType.LBRACE), mutableListOf(
                                                                    MTree(ParserRule(specialRule = "Block"), mutableListOf(
                                                                            MTree(ParserRule(token = TokenType.RBRACE, isEnd = true))
                                                                    ))
                                                            ))
                                                    ))
                                            )),
                                            MTree(ParserRule(token = TokenType.RPAREN), mutableListOf(
                                                    MTree(ParserRule(token = TokenType.RARROW), mutableListOf(
                                                            MTree(ParserRule(specialRule = "Statement", isEnd = true))
                                                    )),
                                                    MTree(ParserRule(token = TokenType.LBRACE), mutableListOf(
                                                            MTree(ParserRule(specialRule = "Block"), mutableListOf(
                                                                    MTree(ParserRule(token = TokenType.RBRACE, isEnd = true))
                                                            ))
                                                    ))
                                            ))
                                    )),
                                    MTree(ParserRule(token = TokenType.RARROW), mutableListOf(
                                            MTree(ParserRule(specialRule = "Statement", isEnd = true))
                                    )),
                                    MTree(ParserRule(token = TokenType.LBRACE), mutableListOf(
                                            MTree(ParserRule(specialRule = "Block"), mutableListOf(
                                                    MTree(ParserRule(token = TokenType.RBRACE, isEnd = true))
                                            ))
                                    ))
                            ))
                    ))
            )
            @Suppress("UNCHECKED_CAST")
            addParserRule("SignOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.SUB, isEnd = true)),
                    MTree(ParserRule(token = TokenType.ADD, isEnd = true))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("ConditionOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.EQUAL, isEnd = true)),
                    MTree(ParserRule(token = TokenType.GREATER, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOWER, isEnd = true)),
                    MTree(ParserRule(token = TokenType.GREATER_EQUAL, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOWER_EQUAL, isEnd = true))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("LowCalcOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.SUB, isEnd = true)),
                    MTree(ParserRule(token = TokenType.ADD, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_AND, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_OR, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_XOR, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_NOT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_LEFT_SHIFT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_RIGHT_SHIFT, isEnd = true))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("HighCalcOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.MUL, isEnd = true)),
                    MTree(ParserRule(token = TokenType.DIV, isEnd = true)),
                    MTree(ParserRule(token = TokenType.MOD, isEnd = true))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("LogicalOperator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.LOGICAL_NOT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOGICAL_AND, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOGICAL_OR, isEnd = true))
            ))
            @Suppress("UNCHECKED_CAST")
            addParserRule("Operator", Operator::class.java as Class<Node>, mutableListOf(
                    MTree(ParserRule(token = TokenType.EQUAL, isEnd = true)),
                    MTree(ParserRule(token = TokenType.GREATER, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOWER, isEnd = true)),
                    MTree(ParserRule(token = TokenType.GREATER_EQUAL, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOWER_EQUAL, isEnd = true)),
                    MTree(ParserRule(token = TokenType.SUB, isEnd = true)),
                    MTree(ParserRule(token = TokenType.ADD, isEnd = true)),
                    MTree(ParserRule(token = TokenType.MUL, isEnd = true)),
                    MTree(ParserRule(token = TokenType.DIV, isEnd = true)),
                    MTree(ParserRule(token = TokenType.MOD, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_AND, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_OR, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_XOR, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_NOT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_LEFT_SHIFT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.BINARY_RIGHT_SHIFT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOGICAL_NOT, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOGICAL_AND, isEnd = true)),
                    MTree(ParserRule(token = TokenType.LOGICAL_OR, isEnd = true))
            ))

            /**
             * Register Parser Statements and Block
             */
            registerParserStatement("ModuleDeclaration")
            registerParserStatement("FunctionDeclaration")
            registerParserStatement("VariableDeclaration")
            registerParserStatement("VariableAssignment")
            registerParserStatement("ExportDeclaration")
            registerParserStatement("ImportDeclaration")
            registerParserStatement("Expression")
            registerParserStatement("ConditionalBranch")

            @Suppress("UNCHECKED_CAST")
            registerParserBlockNode(Block::class.java as Class<Node>)

            isInitialized = true
        }
        @Suppress("UNCHECKED_CAST")
        return Parser(Lexer(input, lexerRules), ParserInfo(parserRules as Map<String, Pair<Class<Node>, ArrayList<MTree<ParserRule>>>>, statements, blockClass))
    }

}
