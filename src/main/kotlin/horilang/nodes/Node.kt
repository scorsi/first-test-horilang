package horilang.nodes

import horilang.lexer.Token

abstract class Node {

    abstract fun build(tokens: List<Token>, nodes: MutableList<Node>): Node

    // abstract fun compute(): Object

    // abstract fun optimize(): Node

}