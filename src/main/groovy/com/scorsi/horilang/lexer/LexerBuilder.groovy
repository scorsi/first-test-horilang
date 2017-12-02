package com.scorsi.horilang.lexer

class LexerBuilder {

    ArrayList<LexerRule> rules = new ArrayList<>()

    LexerBuilder() {
    }

    void addRule(LexerRule rule) {
        rules.add(rule)
    }

    Lexer build(String input) {
        return new Lexer(input, rules)
    }

}
