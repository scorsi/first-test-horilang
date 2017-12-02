package com.scorsi.horilang

import com.scorsi.horilang.ast.AssignmentNode
import com.scorsi.horilang.ast.DeclarationNode
import com.scorsi.horilang.ast.SymbolNode
import com.scorsi.horilang.lexer.LexerRule
import com.scorsi.horilang.parser.ParserRule
import com.scorsi.horilang.parser.ParserRuleTree
import com.scorsi.horilang.parser.ParserRuleContainer

class LiveHorilangInterpreter implements Runnable {

    String input

    LiveHorilangInterpreter(String input) {
        this.input = input
    }

    static List<ParserRuleTree> generateValueRule(ArrayList<ParserRuleTree> nextRule) {
        return generateValueRule(false, null, nextRule)
    }

    static List<ParserRuleTree> generateValueRule(Boolean isEnd, Class node) {
        return generateValueRule(isEnd, node, new ArrayList<ParserRuleTree>())
    }

    static List<ParserRuleTree> generateValueRule(Boolean isEnd, Class node, ArrayList<ParserRuleTree> nextRule) {
        return Arrays.asList(
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.INTEGER), isEnd, node), nextRule),
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.FLOAT), isEnd, node), nextRule),
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.STRING), isEnd, node), nextRule),
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL), isEnd, node), nextRule)
        )
    }

    @Override
    void run() {
        /**
         * Create the Lexer
         */
        def lb = new LexerBuilder()

        lb.addRule(new LexerRule(TokenType.VAR, /var/))
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

        pb.addRule(
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.VAR)), Arrays.asList(
                        new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL), true, DeclarationNode), Arrays.asList( // VAR SYMBOL
                                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.ASSIGN)),
                                        generateValueRule(true, DeclarationNode) // VAR SYMBOL EQUAL value
                                )
                        ))
                ))
        )
        pb.addRule(
                new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.SYMBOL), true, SymbolNode), Arrays.asList( // SYMBOL
                        new ParserRuleTree(new ParserRuleContainer(new ParserRule(token: TokenType.ASSIGN)),
                                generateValueRule(true, AssignmentNode) // SYMBOL ASSIGN value
                        )
                ))
        )

//        try {
        def parser = pb.build(lb.build(input))
        println("Returned: " + parser.parse())
        println("Remaining: " + parser.lexer.tokens)
//        } catch (Error e) {
//            println(e.toString())
//        }


    }

}
