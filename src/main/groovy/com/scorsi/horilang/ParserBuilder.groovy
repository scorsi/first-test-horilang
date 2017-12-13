package com.scorsi.horilang

import com.scorsi.horilang.lexer.Lexer
import com.scorsi.horilang.parser.ParserInfo
import com.scorsi.horilang.parser.ParserRuleTree
import com.scorsi.horilang.parser.Parser
import kotlin.Pair

class ParserBuilder {

    private Map<String, Pair<Class, List<ParserRuleTree>>> rules = new HashMap<>()
    private List<String> statements = new ArrayList<>()
    private Class blockClass = null

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

    void registerBlockNode(Class blockClass) {
        this.blockClass = blockClass
    }

    void registerStatement(String statement) {
        this.statements.push(statement)
    }

    Parser build(Lexer lexer) {
        return new Parser(lexer, new ParserInfo(rules, statements, blockClass))
    }

}
