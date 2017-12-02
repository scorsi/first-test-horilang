package com.scorsi.horilang

import com.scorsi.horilang.lexer.Lexer
import com.scorsi.horilang.parser.ParserRuleTree
import com.scorsi.horilang.parser.Parser

class ParserBuilder {

    ArrayList<ParserRuleTree> rules = new ArrayList<>()

    ParserBuilder() {
    }

    void addRule(ParserRuleTree rule) {
        rules.add(rule)
    }

    Parser build(Lexer lexer) {
        return new Parser(lexer, rules)
    }

}
