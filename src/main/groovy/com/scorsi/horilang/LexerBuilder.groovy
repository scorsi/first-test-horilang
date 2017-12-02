package com.scorsi.horilang

import com.scorsi.horilang.lexer.Lexer
import com.scorsi.horilang.lexer.LexerRule

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
