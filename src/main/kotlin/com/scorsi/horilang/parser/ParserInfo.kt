package com.scorsi.horilang.parser

import com.scorsi.horilang.ast.Node

/**
 * ParserInfo
 *
 * rules: a list of available named rules containing a class and a list of rule
 * statements: a list of "statements" rules
 * block: the class which represents a block of statements
 */
class ParserInfo constructor(
        val rules: Map<String, Pair<Class<Node>, ArrayList<ParserRuleTree<ParserRuleContainer>>>>,
        val statements: List<String>,
        val block: Class<Node>
)
