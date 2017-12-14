package horilang.nodes

import groovy.transform.ToString
import horilang.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class VariableAssignment extends Node {

    String leftValue
    Node rightValue

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        def node = nodes.last()
        if (node != null && !node instanceof Value && !node instanceof Expression)
            throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
        rightValue = node
        nodes.remove(nodes.lastIndexOf(node))
        leftValue = tokens[0].value
        return this
    }

}
