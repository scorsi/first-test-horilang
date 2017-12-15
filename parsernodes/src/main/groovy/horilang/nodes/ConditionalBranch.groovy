package horilang.nodes

import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class ConditionalBranch extends Node {

    Node condition
    Node thenBranch
    Node elseBranch

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        def node = nodes.last()
        if (node == null || !(node instanceof Node))
            throw new RuntimeException("Unexpected parameter, wanted [Block, Statement] but got $node")
        elseBranch = node
        nodes.remove(nodes.lastIndexOf(node))

        node = nodes.last()
        if (node == null)
            throw new RuntimeException("Unexpected parameter, wanted [Condition] but got $node")
        if (node instanceof Expression) {
            condition = node
            thenBranch = elseBranch
            elseBranch = null
        } else if (node instanceof Node) {
            thenBranch = node
            nodes.remove(nodes.lastIndexOf(node))
            node = nodes.last()
            if (node == null || !node instanceof Expression)
                throw new RuntimeException("Unexpected parameter, wanted [Condition] but got $node")
            condition = node
        }
        nodes.remove(nodes.lastIndexOf(node))

        return this
    }

}
