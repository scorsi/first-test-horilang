package com.scorsi.horilang

import com.scorsi.horilang.ast.Block
import com.scorsi.horilang.ast.VariableAssignment
import com.scorsi.horilang.ast.VariableDeclaration

import com.scorsi.horilang.ast.Value
import com.scorsi.horilang.lexer.LexerRule
import com.scorsi.horilang.parser.ParserRule
import com.scorsi.horilang.parser.ParserRuleTree
import com.scorsi.horilang.parser.ParserRuleContainer

class LiveHorilangInterpreter implements Runnable {

    String input

    LiveHorilangInterpreter(String input) {
        this.input = input
    }

    @Override
    void run() {
        /**
         * Create the Lexer
         */
        def lb = new LexerBuilder()

        lb.addRule(new LexerRule(TokenType.VAR, /var/))
        lb.addRule(new LexerRule(TokenType.IF, /if/))
        lb.addRule(new LexerRule(TokenType.SYMBOL, /[a-z]+/))
        lb.addRule(new LexerRule(TokenType.FLOAT, /([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+)/))
        lb.addRule(new LexerRule(TokenType.INTEGER, /[0-9]+/))
        lb.addRule(new LexerRule(TokenType.STRING, /".*"/))
        lb.addRule(new LexerRule(TokenType.DOT, /\./))
        lb.addRule(new LexerRule(TokenType.LPAREN, /\(/))
        lb.addRule(new LexerRule(TokenType.RPAREN, /\)/))
        lb.addRule(new LexerRule(TokenType.LBRACE, /\{/))
        lb.addRule(new LexerRule(TokenType.RBRACE, /\}/))
        lb.addRule(new LexerRule(TokenType.ASSIGN, /\=/))
        lb.addRule(new LexerRule(TokenType.EQUAL, /\==/))
        lb.addRule(new LexerRule(TokenType.GREATER, />/))
        lb.addRule(new LexerRule(TokenType.GREATEREQUAL, />=/))
        lb.addRule(new LexerRule(TokenType.LOWER, /</))
        lb.addRule(new LexerRule(TokenType.LOWEREQUAL, /<=/))
        lb.addRule(new LexerRule(TokenType.EOI, /;+/))
        lb.addRule(new LexerRule(TokenType.EOL, /\n+/))

        /**
         * Create the Parser
         */
        def pb = new ParserBuilder()

        pb.addRule("Declaration", VariableDeclaration,
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.VAR)), Arrays.asList(
                        new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL), true), Arrays.asList( // VAR SYMBOL
                                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.ASSIGN)), Arrays.asList(
                                        new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Value")))
                                ))
                        ))
                ))
        )
        pb.addRule("Assignment", VariableAssignment,
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL)), Arrays.asList(
                        new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.ASSIGN)), Arrays.asList(
                                new ParserRuleTree(new ParserRuleContainer(new ParserRule(specialRule: "Value")))
                        ))
                ))
        )
        pb.addRule("Value", Value, Arrays.asList(
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.INTEGER), true)),
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.FLOAT), true)),
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.STRING), true)),
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL), true))
        ))

        pb.registerStatement("Assignment")
        pb.registerStatement("Declaration")

        pb.registerBlockNode(Block)

//        try {
        def parser = pb.build(lb.build(input))
        println("Returned: " + parser.parse())
        println("Remaining: " + parser.lexer.tokens)
//        } catch (Error e) {
//            println(e.toString())
//        }


    }

}
