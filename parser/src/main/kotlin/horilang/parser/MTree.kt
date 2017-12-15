package horilang.parser

data class MTree<out T> @JvmOverloads constructor(val value: T, val children: List<MTree<T>> = emptyList())