package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class VariableDeclaration extends Node {

    String symbol
    Node rightValue = null

    VariableDeclaration(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        if (!nodes.isEmpty()) {
            def node = nodes.last()
            if (!node instanceof Value && !node instanceof Expression)
                throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
            rightValue = node
            nodes.remove(nodes.lastIndexOf(node))
        }
        symbol = tokens[1].value
    }

}
