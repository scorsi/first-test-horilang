package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class Expression extends Node {

    Node leftValue
    Node operator = null
    Node rightValue = null

    Expression(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        def node = nodes.last()
        if (node == null || !(node instanceof Value || node instanceof Expression))
            throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
        rightValue = node
        nodes = nodes.dropRight(1)

        if (nodes.isEmpty()) {
            leftValue = rightValue
            rightValue = null
            return this
        }
        node = nodes.last()
        if (!node instanceof Operator)
            throw new RuntimeException("Unexpected parameter, wanted [Operator] but got $node")
        operator = node
        nodes = nodes.dropRight(1)
        node = nodes.last()
        if (!node instanceof Value && !node instanceof Expression)
            throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
        leftValue = node
        nodes = nodes.dropRight(1)
    }

}
