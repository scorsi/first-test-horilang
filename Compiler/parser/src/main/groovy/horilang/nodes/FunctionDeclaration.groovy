package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@EqualsAndHashCode
class FunctionDeclaration extends Node {

    String symbol
    FunctionArguments functionArguments
    Node block

    FunctionDeclaration(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        symbol = tokens[1].value
        def node = nodes.last()
        if (node == null || !node instanceof Node)
            throw new RuntimeException("Unexpected parameter, wanted [Block, Statement] but got $node")
        block = node
        nodes.remove(nodes.last())

        if (nodes.isEmpty())
            return
        node = nodes.last()
        if (node == null && !node instanceof FunctionArguments)
            throw new RuntimeException("Unexpected parameter, wanted [Block] but got $node")
        functionArguments = node as FunctionArguments
        nodes.remove(nodes.last())
    }

    List<VariableDeclaration> getArguments() {
        return functionArguments?.arguments
    }

    @Override
    String toString() {
        if (arguments == null || arguments.isEmpty())
            return "FunctionDeclaration(symbol:$symbol, block:$block)"
        else
            return "FunctionDeclaration(symbol:$symbol, arguments:$arguments, block:$block)"
    }

}
