package horilang.lexer

fun String.matchesRule(rule: LexerRule): Boolean =
        matches(rule.match.toRegex())