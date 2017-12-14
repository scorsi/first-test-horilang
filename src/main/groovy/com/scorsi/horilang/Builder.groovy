package com.scorsi.horilang

import com.scorsi.horilang.ast.Block
import com.scorsi.horilang.ast.Expression
import com.scorsi.horilang.ast.Operator
import com.scorsi.horilang.ast.Value
import com.scorsi.horilang.ast.VariableAssignment
import com.scorsi.horilang.ast.VariableDeclaration
import com.scorsi.horilang.lexer.Lexer
import com.scorsi.horilang.lexer.LexerRule
import com.scorsi.horilang.parser.Parser
import com.scorsi.horilang.parser.ParserInfo
import com.scorsi.horilang.parser.ParserRule
import com.scorsi.horilang.parser.ParserRuleContainer
import com.scorsi.horilang.parser.ParserRuleTree
import kotlin.Pair

class Builder {

    private static ArrayList<LexerRule> lexerRules = new ArrayList<>()
    private static Map<String, Pair<Class, List<ParserRuleTree>>> parserRules = new HashMap<>()
    private static List<String> statements = new ArrayList<>()
    private static Class blockClass = null

    private static Boolean isInitialized = false

    private static void addLexerRule(LexerRule rule) {
        lexerRules.add(rule)
    }

    private static void addParserRule(String key, Class c, ParserRuleTree rule) {
        if (parserRules[key] == null)
            parserRules[key] = new Pair<>(c, new ArrayList<>())
        parserRules[key].second.add(rule)
    }

    private static void addParserRule(String key, Class c, List<ParserRuleTree> rule) {
        if (parserRules[key] == null)
            parserRules[key] = new Pair<>(c, new ArrayList<>())
        parserRules[key].second.addAll(rule)
    }

    private static void registerParserBlockNode(Class blockClass) {
        this.blockClass = blockClass
    }

    private static void registerParserStatement(String statement) {
        statements.push(statement)
    }


    static Parser build(String input) {
        if (!isInitialized) {
            /**
             * Create all the Lexer Rules
             */
            addLexerRule(new LexerRule(TokenType.VAR, /var/))
            addLexerRule(new LexerRule(TokenType.IF, /if/))
            addLexerRule(new LexerRule(TokenType.FLOAT, /([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+)/))
            addLexerRule(new LexerRule(TokenType.INTEGER, /[0-9]+/))
            addLexerRule(new LexerRule(TokenType.SYMBOL, /[a-z]+/))
            addLexerRule(new LexerRule(TokenType.STRING, /".*"/))
            addLexerRule(new LexerRule(TokenType.DOT, /\./))
            addLexerRule(new LexerRule(TokenType.LPAREN, /\(/))
            addLexerRule(new LexerRule(TokenType.RPAREN, /\)/))
            addLexerRule(new LexerRule(TokenType.LBRACE, /\{/))
            addLexerRule(new LexerRule(TokenType.RBRACE, /\}/))
            addLexerRule(new LexerRule(TokenType.ASSIGN, /\=/))
            addLexerRule(new LexerRule(TokenType.EQUAL, /\==/))
            addLexerRule(new LexerRule(TokenType.GREATER, />/))
            addLexerRule(new LexerRule(TokenType.GREATEREQUAL, />=/))
            addLexerRule(new LexerRule(TokenType.LOWER, /</))
            addLexerRule(new LexerRule(TokenType.LOWEREQUAL, /<=/))
            addLexerRule(new LexerRule(TokenType.SUB, /-/))
            addLexerRule(new LexerRule(TokenType.ADD, /\+/))
            addLexerRule(new LexerRule(TokenType.MUL, /\*/))
            addLexerRule(new LexerRule(TokenType.DIV, /\//))
            addLexerRule(new LexerRule(TokenType.MOD, /%/))
            addLexerRule(new LexerRule(TokenType.EOI, /;+/))
            addLexerRule(new LexerRule(TokenType.EOL, /\n+/))

            /**
             * Create all the Parser Rules
             */
            addParserRule("Declaration", VariableDeclaration,
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.VAR)), Arrays.asList(
                            new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL), true), Arrays.asList( // VAR SYMBOL
                                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.ASSIGN)), Arrays.asList(
                                            new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Expression"))),
                                            new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Value")))
                                    ))
                            ))
                    ))
            )
            addParserRule("Assignment", VariableAssignment,
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL)), Arrays.asList(
                            new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.ASSIGN)), Arrays.asList(
                                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Expression"))),
                                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Value")))
                            ))
                    ))
            )
            addParserRule("Value", Value, Arrays.asList(
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.INTEGER))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.FLOAT))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.STRING))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL)))
            ))
            addParserRule("Expression", Expression, Arrays.asList(
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Value"), true), Arrays.asList(
                            new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Operator")), Arrays.asList(
                                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Expression")))
                            )),
                    )),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.LPAREN)), Arrays.asList(
                            new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Expression")), Arrays.asList(
                                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.RPAREN), true), Arrays.asList(
                                            new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Operator")), Arrays.asList(
                                                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Expression")))
                                            ))
                                    ))
                            ))
                    )),
            ))
            addParserRule("Operator", Operator, Arrays.asList(
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.EQUAL))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.GREATER))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.LOWER))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.GREATEREQUAL))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.LOWEREQUAL))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SUB))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.ADD))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.MUL))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.DIV))),
                    new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.MOD)))
            ))

            /**
             * Register Parser Statements and Block
             */
            registerParserStatement("Assignment")
            registerParserStatement("Declaration")
            registerParserStatement("Value")
            registerParserStatement("Expression")

            registerParserBlockNode(Block)


            isInitialized = true
        }
        return new Parser(new Lexer(input, lexerRules), new ParserInfo(parserRules, statements, blockClass))
    }

}
