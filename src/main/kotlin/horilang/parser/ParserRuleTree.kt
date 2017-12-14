package horilang.parser

data class ParserRuleTree<out T> @JvmOverloads constructor(val value: T, val children: List<ParserRuleTree<T>> = emptyList())