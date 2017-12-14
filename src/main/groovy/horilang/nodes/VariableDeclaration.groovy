package horilang.nodes

import groovy.transform.ToString
import horilang.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class VariableDeclaration extends Node {

    String symbol
    Node rightValue = null

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        if (!nodes.isEmpty()) {
            def node = nodes.last()
            if (!node instanceof Value && !node instanceof Expression)
                throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
            rightValue = node
            nodes.remove(nodes.lastIndexOf(node))
        }
        symbol = tokens[1].value
        return this
    }

}
