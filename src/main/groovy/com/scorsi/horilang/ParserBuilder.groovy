package com.scorsi.horilang

import com.scorsi.horilang.lexer.Lexer
import com.scorsi.horilang.parser.ParserRuleTree
import com.scorsi.horilang.parser.Parser
import kotlin.Pair

class ParserBuilder {

    Map<String, Pair<Class, List<ParserRuleTree>>> rules = new HashMap<>()

    ParserBuilder() {
    }

    void addRule(String key, Class c, ParserRuleTree rule) {
        if (rules[key] == null)
            rules[key] = new Pair<>(c, new ArrayList<>())
        rules[key].second.add(rule)
    }

    void addRule(String key, Class c, List<ParserRuleTree> rule) {
        if (rules[key] == null)
            rules[key] = new Pair<>(c, new ArrayList<>())
        rules[key].second.addAll(rule)
    }

    Parser build(Lexer lexer) {
        return new Parser(lexer, rules)
    }

}
