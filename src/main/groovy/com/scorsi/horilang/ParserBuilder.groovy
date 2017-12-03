package com.scorsi.horilang

import com.scorsi.horilang.lexer.Lexer
import com.scorsi.horilang.parser.ParserRuleTree
import com.scorsi.horilang.parser.Parser
import kotlin.Pair

class ParserBuilder {

    Map<String, Pair<Class, ArrayList<ParserRuleTree>>> rules = new HashMap<>()

    ParserBuilder() {
    }

    void addRule(String key, Class c,ParserRuleTree rule) {
        if (rules[key] == null)
            rules[key] = new Pair<>(c, new ArrayList<>())
        rules[key].second.add(rule)
    }

    Parser build(Lexer lexer) {
        return new Parser(lexer, rules)
    }

}
