package horilang.nodes

import groovy.transform.ToString
import horilang.lexer.Token
import horilang.nodes.Node
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class VariableAssignment extends Node {

    String leftValue
    Node rightValue

    VariableAssignment(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        def node = nodes.last()
        if (node != null && !node instanceof Value && !node instanceof Expression)
            throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
        rightValue = node
        nodes.remove(nodes.lastIndexOf(node))
        leftValue = tokens[0].value
    }

}
