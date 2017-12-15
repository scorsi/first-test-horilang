package horilang.nodes

import horilang.lexer.Token

@Suppress("UNUSED_PARAMETER")
abstract class Node constructor(val tokens: List<Token>, nodes: MutableList<Node>) {

    // abstract fun optimize(): Node

    // abstract fun validate(): Boolean

}