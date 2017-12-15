package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class FunctionArguments extends Node {

    LinkedList<VariableDeclaration> arguments = new LinkedList<>()

    FunctionArguments(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        def node = nodes.last()
        if (node == null)
            throw new RuntimeException("Unexpected parameter, wanted [FunctionArguments, VariableDeclaration] but got $node")

        if (node instanceof FunctionArguments)
            arguments = node.arguments
        else if (node instanceof VariableDeclaration)
            while (node != null && node instanceof VariableDeclaration) {
                arguments.addFirst(node as VariableDeclaration)
                nodes.remove(nodes.last())
                if (nodes.isEmpty()) node = null
                else node = nodes.last()
            }
        else
            throw new RuntimeException("Unexpected parameter, wanted [FunctionArguments, VariableDeclaration] but got $node")

        if (node != null)
            nodes.remove(nodes.last())
    }

}
